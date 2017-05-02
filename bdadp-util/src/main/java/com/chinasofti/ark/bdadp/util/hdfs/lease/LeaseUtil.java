package com.chinasofti.ark.bdadp.util.hdfs.lease;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Including methods to renew lease of specified cluster.
 */
public class LeaseUtil {

    private static final Logger LOG = LoggerFactory.getLogger(LeaseUtil.class);

    /**
     * Renew lease of cluster by the given conf.
     */
    public static void connect(Configuration conf) throws IOException {
        UserGroupInformation.setConfiguration(conf);
        if (UserGroupInformation.isLoginKeytabBased()) {
            UserGroupInformation.getLoginUser().reloginFromKeytab();
            LOG.info("Kerberos relogin successfully.");
        } else {
            UserGroupInformation.loginUserFromKeytab(conf.get("username.client.kerberos.principal"),
                    conf.get("username.client.keytab.file"));
            LOG.info("Kerberos login successfully.");
        }
    }
}
