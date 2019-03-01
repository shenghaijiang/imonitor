package cn.xtits.imonitor.shiro;

import cn.xtits.imonitor.interceptor.LoginToken;
import cn.xtits.imonitor.sapi.xtp.ShiroDto;
import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.utils.JwtUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import io.jsonwebtoken.Claims;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import cn.xtits.imonitor.sapi.xtp.ShiroService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MyRealm extends AuthorizingRealm {

    @Autowired
    private StringRedisTemplate template;

    private static final Logger LOGGER = LogManager.getLogger(MyRealm.class);

    @Autowired
    private ShiroService shiroService;


    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        {
            Claims claims = null;
            try {
                claims = JwtUtil.parseJWT(principals.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            LoginToken t = JsonUtil.fromJson(claims.getSubject(), LoginToken.class);
            int userId = t.getUserId();
            String toJson = "";
            ValueOperations<String, String> ops = template.opsForValue();
            String key = "auth:" + String.valueOf(userId);
            try {
                if (!template.hasKey(key)) {
                    AjaxResult result = shiroService.listMenuWithOperationByUserId(userId, principals.toString());
                    toJson = JsonUtil.toJson(result.getData());
                    ops.set(key, toJson, 60, TimeUnit.MINUTES);
                } else {
                    toJson = ops.get(key);
                }
            } catch (Exception e) {
                AjaxResult result = shiroService.listMenuWithOperationByUserId(userId, principals.toString());
                toJson = JsonUtil.toJson(result.getData());
            }
            ShiroDto shiroDto = JsonUtil.fromJson(toJson, ShiroDto.class);
            Set<String> permission = new HashSet<>();
    for (String s : shiroDto.getPermissions().split("\\|")) {
    permission.add(s);
    }
    SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
    simpleAuthorizationInfo.addRole(shiroDto.getRoles());
    simpleAuthorizationInfo.addStringPermissions(permission);
    return simpleAuthorizationInfo;
    }
    }

    /**
    * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
    */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
    String token = (String) auth.getCredentials();
    // 解密获得username，用于和数据库进行对比
    //        String username = JWTUtil.getUsername(token);
    //        if (username == null) {
    //            throw new AuthenticationException("token invalid");
    //        }
    //
    //        UserBean userBean = service.getUser(username);
    //        if (userBean == null) {
    //            throw new AuthenticationException("User didn't existed!");
    //        }
    //
    //        if (! JWTUtil.verify(token, username, userBean.getPassword())) {
    //            throw new AuthenticationException("Username or password error");
    //        }

    return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
    }
