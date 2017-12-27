package com.flowable.web.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flowable.core.bean.ProcessVariable;
import com.flowable.core.service.IProcessVariableService;
import com.flowable.core.util.Constants;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.flowable.common.exception.ServiceException;
import com.flowable.common.utils.DataGrid;
import com.flowable.common.utils.Json;
import com.flowable.common.utils.PageHelper;
import com.flowable.core.bean.BizInfo;
import com.flowable.core.service.IBizInfoService;
import com.flowable.core.service.IProcessDefinitionService;
import com.flowable.core.service.IProcessExecuteService;
import com.flowable.core.util.WebUtil;

@Controller
@RequestMapping("/workflow")
public class ProcessExecuteController {

    @Autowired
    private IProcessExecuteService processExecuteService;

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    @Autowired
    private IBizInfoService bizInfoService;

    @Autowired
    private IProcessVariableService processVariableService;

    @Value("${web.maxUploadSize}")
    private long maxUpload;

    private Logger logger = LoggerFactory.getLogger("processExecuteController");

    @ResponseBody
    @RequestMapping(value = "/loadWorkLogInput")
    public Map<String, Object> loadWorkLogInput(String logId) {
        return processExecuteService.loadBizLogInput(logId);
    }

    @RequestMapping(value = "index")
    public String index(Model model) {

        Map<String, Object> map = processExecuteService.loadProcessList();
        model.addAttribute("ProcessMapJson", JSONObject.toJSONString(map));
        return "work/index";
    }

    /**
     * target取值如下<br>
     * myComplete : 待办工单<br>
     * myClaim : 待签任务<br>
     * query : 全局查询<br>
     * myCreate : 我创建的单<br>
     * myHandle : 我处理过的单<br>
     * myClose : 我创建并关闭的单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryWorkOrder")
    public DataGrid queryWorkOrder(@RequestParam Map<String, Object> params, PageHelper<BizInfo> page,
                                   HttpServletRequest request, HttpServletResponse response) {

        WebUtil.getLoginUser(request, response);
        String action = (String) params.get("action");
        PageHelper<BizInfo> helper = processExecuteService.queryMyBizInfos(action, params, page);
        DataGrid grid = new DataGrid();
        grid.setRows(helper.getList());
        grid.setTotal(helper.getCount());
        return grid;
    }

    /**
     * 创建工单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create/{key}", method = RequestMethod.GET)
    public Map<String, Object> create(@PathVariable("key") String key, HttpServletRequest request) {

        Map<String, Object> data = new HashMap<String, Object>();
        try {

            WebUtil.getLoginUser(request);
            ProcessDefinition processDefinition = processDefinitionService.getLatestProcDefByKey(key);
            if (processDefinition != null) {
                String proDefId = processDefinition.getId();
                data.put("base_tempID", proDefId);
                ProcessVariable variable = new ProcessVariable();
                variable.setProcessDefinitionId(proDefId);
                variable.setTaskId(Constants.TASK_START);
                variable.setVersion(-1);
                List<ProcessVariable> list = this.processVariableService.findProcessVariables(variable);
                data.put("SYS_BUTTON", processExecuteService.loadStartButtons(proDefId));
                Map<String, List<ProcessVariable>> map = groupProcessValBean(list);
                data.put("ProcessValBeanMap", map);
                data.put("result", true);
            } else {
                data.put("result", false);
                data.put("msg", "流程【" + key + "】未找到!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 显示某个工单信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/display/{id}")
    public Map<String, Object> display(@PathVariable("id") String id, HttpServletRequest request) {

        Map<String, Object> data = new HashMap<String, Object>();
            Map<String, Object> result = processExecuteService.queryWorkOrder(id);
        try {
            WebUtil.getLoginUser(request);
            logger.info(JSONObject.toJSONString(result));
            String processVal = JSONObject.toJSONString(result.get("processVariables"));
            List<ProcessVariable> list = JSON.parseObject(processVal, new TypeReference<List<ProcessVariable>>() {
            });
            Map<String, List<ProcessVariable>> map = groupProcessValBean(list);
            result.put("processVariablesMap", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 创建工单
     *
     * @param params
     * @param startProc
     * @param deleFileId
     * @param request
     * @return
     */
    @RequestMapping(value = "bizInfo/create")
    public ResponseEntity<String> createBiz(@RequestParam Map<String, Object> params, boolean startProc,
                                            String[] deleFileId, MultipartHttpServletRequest request) {

        Json json = new Json();
        WebUtil.getLoginUser(request);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.TEXT_PLAIN);
        BizInfo bean = null;
        try {
            if (validateFileSize(request)) {
                json.setSuccess(false);
                json.setMsg("操作失败: " + "附件大小不能超过" + maxUpload / 1024 / 1024 + "M");
                return new ResponseEntity<String>(JSONObject.toJSONString(json), header, HttpStatus.OK);
            }
            bean = processExecuteService.createBizDraft(params, request.getMultiFileMap(), startProc, deleFileId);
        } catch (Exception e) {
            logger.error("工单创建失败 : {}", e);
            json.setSuccess(false);
            json.setMsg("操作失败: " + e.getLocalizedMessage());
            return new ResponseEntity<String>(JSONObject.toJSONString(json), header, HttpStatus.OK);
        }
        json.setSuccess(true);
        if (startProc) {
            json.setMsg("/biz/" + bean.getId());
        } else {
            json.setMsg("/biz/list/myWork");
        }
        json.setSuccess(true);
        return new ResponseEntity<String>(JSONObject.toJSONString(json), header, HttpStatus.OK);
    }

    /**
     * 重新提交
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "bizInfo/updateBiz")
    public ResponseEntity<String> updateBiz(@RequestParam Map<String, Object> params, MultipartHttpServletRequest request) {

        Json json = new Json();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        try {
            WebUtil.getLoginUser(request);
            if (validateFileSize(request)) {
                json.setMsg("操作失败: " + "附件大小不能超过" + maxUpload / 1024 / 1024 + "M");
                return new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
            }
            processExecuteService.updateBiz(params, request.getMultiFileMap());
        } catch (Exception e) {
            logger.info("error", e);
            json.setSuccess(false);
            json.setMsg("操作失败: " + e.getLocalizedMessage());
            return new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
        }
        json.setSuccess(true);
        json.setMsg("操作成功");
        return new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
    }

    /**
     * 工单处理，自动处理工单的各种状态的提交
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/submit")
    public ResponseEntity<String> submit(@RequestParam Map<String, Object> params, MultipartHttpServletRequest request) {

        Json json = new Json();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        try {
            WebUtil.getLoginUser(request);
            json.setSuccess(false);
            if (validateFileSize(request)) {
                json.setMsg("操作失败: " + "附件大小不能超过" + maxUpload / 1024 / 1024 + "M");
                return new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
            }
            processExecuteService.submit(params, request.getMultiFileMap());
        } catch (Exception e) {
            logger.error("表单提交失败 : {}", e);
            json.setSuccess(false);
            json.setMsg("操作失败: " + e.getLocalizedMessage());
            return new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
        }

        json.setSuccess(true);
        json.setMsg("操作成功");
        return new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
    }

    private boolean validateFileSize(MultipartHttpServletRequest request) {

        long fileSize = 0L;
        MultiValueMap<String, MultipartFile> multiValueMap = request.getMultiFileMap();
        if (MapUtils.isNotEmpty(multiValueMap)) {
            Collection<List<MultipartFile>> files = multiValueMap.values();
            for (List<MultipartFile> list : files) {
                for (MultipartFile multipartFile : list) {
                    fileSize += multipartFile.getSize();
                }
            }
        }
        if (maxUpload < fileSize) {
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping(value = "/bizInfo/delete")
    public Json deleteBizInfo(@RequestParam List<String> ids) {

        Json json = new Json();
        try {
            bizInfoService.updateBizByIds(ids);
        } catch (ServiceException e) {
            logger.error("操作失败 : {}", e);
            json.setSuccess(false);
            json.setMsg("操作失败: " + e.getLocalizedMessage());
            return json;
        }
        json.setSuccess(true);
        json.setMsg("操作成功");
        return json;
    }

    @ResponseBody
    @RequestMapping(value = "/download")
    public void downloadFile(String action, String id, HttpServletRequest request, HttpServletResponse response) {

        Object[] result = processExecuteService.downloadFile(action, id);
        if (result[1] == null) {
            return;
        }
        InputStream is = (InputStream) result[1];
        String fileType = (String) result[0];
        Long fileLong = (Long) result[2];
        String fileName = (String) result[3];
        response.reset();
        if ("IMAGE".equalsIgnoreCase(fileType)) {
            response.setContentType("image/PNG;charset=GB2312");
        } else {
            try {
                fileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/x-download");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-Length", fileLong == null ? "0" : String.valueOf(fileLong));
        }
        if (is != null) {
            OutputStream output = null;
            try {
                output = response.getOutputStream();// 得到输出流
                int size = 2048;
                byte[] b = new byte[size];
                int p = 0;
                while ((p = is.read(b)) > 0) {
                    output.write(b, 0, p);
                    if (p < size) {
                        break;
                    }
                }
            } catch (Exception e) {
            } finally {
                try {
                    is.close();// 关闭文件流

                } catch (Exception e) {
                }
                try {
                    output.flush();
                    output.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 将属性进行分组
     *
     * @param list
     * @return
     */
    private static Map<String, List<ProcessVariable>> groupProcessValBean(List<ProcessVariable> list) {

        Map<String, List<ProcessVariable>> map = new LinkedHashMap<String, List<ProcessVariable>>();
        for (ProcessVariable bean : list) {
            String groupName = bean.getGroupName();
            groupName = StringUtils.isEmpty(groupName) ? "其它信息" : groupName;
            List<ProcessVariable> temp = map.get(groupName);
            if (temp == null) {
                temp = new ArrayList<ProcessVariable>();
                map.put(groupName, temp);
            }
            temp.add(bean);
        }
        return map;
    }
}
