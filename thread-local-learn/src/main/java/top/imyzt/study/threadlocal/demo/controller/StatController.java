package top.imyzt.study.threadlocal.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyzt.study.threadlocal.demo.Val;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模型适用于需要接收大量并发请求,然后汇总请求其它接口的情况.
 * 避免进入临界区, 空间换时间
 * @author imyzt
 * @date 2020/02/09
 */
@RestController
@RequestMapping
public class StatController {

    // 线程安全问题. 可以通过加同步锁解决addSet方法
//     private static final Set<Val<Integer>> VAL_SET = new HashSet<>();
    // 线程安全
     private static final Set<Val<Integer>> VAL_SET = ConcurrentHashMap.newKeySet();

    private static synchronized void addSet(Val<Integer> v) {
        VAL_SET.add(v);
    }

    private static ThreadLocal<Val<Integer>> local = ThreadLocal.withInitial(() -> {
        Val<Integer> val = new Val<>();
        val.set(0);
        // 线程安全问题
        // set.add(val);
        addSet(val);
        return val;
    });

    @GetMapping("stat")
    public Integer stat() {
        return VAL_SET.stream().map(Val::get).reduce(Integer::sum).get();
    }

    @GetMapping("add")
    public Integer add() {
        // 当前线程处理的总数
        Val<Integer> val = local.get();
        // 自增然后放进线程本地变量
        val.set(val.get() + 1);
        return 1;
    }

    @GetMapping("clear")
    public Integer clear() {
        VAL_SET.stream().forEach(d -> d.set(0));
        return 1;
    }
}
