package top.imyzt.agent.proxy;


/**
 * @author imyzt
 * @date 2024/08/11
 * @description 目标代理类
 */
public class UserService {

    //方法1
    @Log(prefix = "username")
    public String username(String username){
        System.out.println(username);
        return username;
    }

    //方法2
    public String address(String username){
        System.out.println("address(String username).....");
        return username+"来自 【湖北省-仙居-恩施土家族苗族自治州】";
    }

    //方法3
    @Log(prefix = "address2")
    public String address(String username,String city){
        System.out.println("address(String username,String city).....");
        return username+"来自 【湖北省"+city+"】";
    }
}
