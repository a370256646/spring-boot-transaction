package com.lc.transaction.reflex;

import com.lc.transaction.common.mysql.model.DistributionTaskInvoke;
import com.lc.transaction.service.TransactionService;

/**
 * @author liucheng
 * @create 2018-05-16 10:05
 **/
public class BeanInvoke {

    public static DistributionTaskInvoke invoke(String path,Object... objects) throws Exception {
        TransactionService service = (TransactionService) Reflection.newInstance(path);
        return service.execute(objects);
    }
    public static DistributionTaskInvoke invoke(Class clz,Object... objects) throws Exception {
        TransactionService service = (TransactionService) Reflection.newInstance(clz);
        return service.execute(objects);
    }
}
