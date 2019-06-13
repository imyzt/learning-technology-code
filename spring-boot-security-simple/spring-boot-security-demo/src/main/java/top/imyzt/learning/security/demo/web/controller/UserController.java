package top.imyzt.learning.security.demo.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.imyzt.learning.security.demo.dto.User;
import top.imyzt.learning.security.demo.dto.UserQueryCondition;
import top.imyzt.learning.security.demo.exception.UserNotExistException;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * @author imyzt
 * @date 2019/6/1
 * @description UserController
 */
@RestController
@RequestMapping("user")
@Api(value = "用户服务")
@Slf4j
public class UserController {


    @GetMapping("/me")
    public Object getCurrentUser(Authentication authentication/*@AuthenticationPrincipal UserDetails userDetails*/) {
        // 可直接通过入参拿到authentication, 也可通过SecurityContextHolder对象获取, 通过@AuthenticationPrincipal注解可以只拿principal对象
//        return userDetails;
        return authentication;
//         return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping
    @JsonView(User.UserSimpleView.class)
    public List<User> getUserList(UserQueryCondition condition,
                              @PageableDefault(size = 14, page = 2, sort = "username,asc") Pageable pageable) {

        log.info(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));
        log.info(pageable.toString());

        return Collections.singletonList(new User().setUsername("yzt").setPassword("xxx"));
    }

    @GetMapping("{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    @ApiOperation(value = "用户查询服务")
    public User getInfo(@ApiParam(value = "用户id") @PathVariable String id) {

        if (id.length() <= 1) {
            throw new UserNotExistException(id);
        }

        System.out.println("进入getInfo服务");

        System.out.println(id);

        User user = new User();
        user.setUsername("tom").setPassword("xxx");
        return user;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult errors) {

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(System.err::println);
        }

        System.out.println(user);

        return user.setId("1");
    }

    @PutMapping("{id:\\d+}")
    public User create(@PathVariable String id,
                       @Valid @RequestBody User user, BindingResult errors) {

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> {
                System.out.println(error.getDefaultMessage());
            });
        }

        System.out.println(user);

        return user.setId("1");
    }

    @DeleteMapping("{id:\\d+}")
    public void create(@PathVariable String id) {
        System.out.println(id);
    }
}