package top.imyzt.learning.springboot.checkpoint;

import org.crac.CheckpointException;
import org.crac.Core;
import org.crac.RestoreException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author imyzt
 * @date 2023/07/09
 * @description 描述信息
 */
@RestController
@RequestMapping("checkpoint")
public class CheckpointController {

    @GetMapping
    public void checkpoint() {

        try {
            Core.checkpointRestore();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (CheckpointException e) {
            e.printStackTrace();
        } catch (RestoreException e) {
            e.printStackTrace();
        }

    }
}