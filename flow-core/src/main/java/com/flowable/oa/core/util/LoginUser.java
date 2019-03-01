package com.flowable.oa.core.util;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lukew
 * @eamil 13507615840@163.com
 * @create 2018-09-06 19:06
 **/
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = -60105333016879224L;

    private String id;

    private String username;

    private String name;

    private String mobile;

    private String email;

    private List<String> urls = new ArrayList<>(0);

    private Set<String> roles = new HashSet<>();
}
