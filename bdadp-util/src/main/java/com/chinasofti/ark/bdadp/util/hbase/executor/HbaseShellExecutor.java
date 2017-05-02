package com.chinasofti.ark.bdadp.util.hbase.executor;

import com.chinasofti.ark.bdadp.util.hbase.common.HShellStringParser;
import com.chinasofti.ark.bdadp.util.hbase.common.HbaseClient;
import com.chinasofti.ark.bdadp.util.hbase.response.HbaseShellRsp;
import com.chinasofti.ark.bdadp.util.hdfs.common.ConfigurationClient;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Executor used to do shell relative operations.
 */
public class HbaseShellExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(HbaseShellExecutor.class);
    private static final String HTML_LINESEP = "</br>";
    private static String tips;
    private static Configuration CONF;
    private Admin client; // hbase client.
    private HShellStringParser parser;

    public HbaseShellExecutor() {
        init();
    }

    public static void main(String[] args) {
        String shell = "hbase admin -deleteTable test_chenwang";
        HbaseShellRsp rsp = new HbaseShellRsp();
        HbaseShellExecutor executor = new HbaseShellExecutor();
        executor.execute(shell, rsp);
        System.out.println(">>> " + rsp.toString());
    }

    private void init() {
        client = HbaseClient.getAdmin();
        CONF = ConfigurationClient.getInstance().getConfiguration();
        initTipMsgs();
    }

    private void initTipMsgs() {
        StringBuilder sb = new StringBuilder();
        sb.append("Command not found! Use <b>'hbase -help'</b> to see more:").append(HTML_LINESEP)
                .append("eg. ")
                .append("hbase admin -createTable table_name:coulumn_family1:column_family2")
                .append(HTML_LINESEP)
                .append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp")
                .append("hbase table -put Table:Rowkey:CF$Column:value");
        tips = sb.toString();
    }

    /**
     * Execute the given shell
     *
     * @return <code>true</code> if succeeded, <code>false</code> otherwise.
     */
    public boolean execute(String shell, HbaseShellRsp rsp) {
        try {
            shell = shell.trim();
            preCheck(shell, rsp);
            doAction(shell, rsp);
            return true;
        } catch (Exception e) {
            LOG.error("Shell executing error: ", e);
            rsp.recordError(e);
            return false;
        }
    }

    private void doAction(String shell, HbaseShellRsp rsp) throws IOException {
        if (parser.getOpType().equals("-help")) {
            showHelp(rsp);
            return;
        }
        String op = parser.getOperation();
        if (op.equals("createTable")) {
            doCreateTable(rsp);
        } else if (op.equals("deleteTable")) {
            doDeleteTable(rsp);
        } else if (op.equals("deleteTable")) {

        } else {
            LOG.error("Unknow operation: []", op);
            throw new RuntimeException("Unknown operation: " + op);
        }

    }

    private void showHelp(HbaseShellRsp rsp) {
        rsp.appendResult(getHelpList());
    }

    private String getHelpList() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "hbase admin -createTable Table:CF1:CF2		-->create table with given name and multi column families")
                .append(HTML_LINESEP)
                .append(
                        "hbase admin -deleteTable Table					-->delete table by the given name")
                .append(HTML_LINESEP)
                .append(
                        "hbase table -put Table:Rowkey:CF$Column:value	-->insert one row to the specified table")
                .append(HTML_LINESEP)
                .append(
                        "hbase table -batchPut Table:FilePath			-->insert batch datas into specified table, batch data should be accessed from local OS")
                .append(HTML_LINESEP)
                .append(
                        "hbase table -delete Table:Rowkey				-->delete one row from the specified table")
                .append(HTML_LINESEP)
                .append(
                        "hbase fs -export Table:TargetPath				-->export the specified table to the given path")
                .append(HTML_LINESEP)
                .append(
                        "hbase -help									-->see help for hbase shell commands");
        return sb.toString();
    }

    private void doDeleteTable(HbaseShellRsp rsp) throws IOException {
        String tabName = parser.getOpParameters();
        if (!client.tableExists(TableName.valueOf(tabName))) {
            LOG.warn("Table [{}] not exist!", tabName);
            rsp.appendResult("Table not exist!");
            return;
        }
        if (!client.isTableDisabled(TableName.valueOf(tabName))) {
            client.disableTable(TableName.valueOf(tabName));
        }
        client.deleteTable(TableName.valueOf(tabName));
        rsp.appendResult("Delete table successful! \n Deleted table: " + tabName);
    }

    private void doCreateTable(HbaseShellRsp rsp) throws IOException {
        String[] strs = parser.getOpParameters().split(HShellStringParser.PARA_SEP);
        String tableName = strs[0];

        if (client.tableExists(TableName.valueOf(tableName))) {
            LOG.warn("Table already exist.");
            rsp.appendResult("Table already exist.");
            return;
        }
        HTableDescriptor des = new HTableDescriptor(TableName.valueOf(tableName));
        for (int i = 1; i < strs.length; i++) {
            HColumnDescriptor hc = new HColumnDescriptor(strs[i]);
            hc.setDataBlockEncoding(DataBlockEncoding.FAST_DIFF);
            hc.setCompressionType(Compression.Algorithm.SNAPPY);
            des.addFamily(hc);
        }
        client.createTable(des);
        HTableDescriptor tableinfo = client.getTableDescriptor(TableName.valueOf(tableName));
        rsp.appendResult("Create table successful! </br> Table infos: </br>" + tableinfo);
    }

    public void doAddRow(HbaseShellRsp rsp) throws Exception {
        String[] strs = parser.getOpParameters().split("'");
        String tableName = strs[1];
        String[] strss = strs[3].split(":");

        HTable table = new HTable(CONF, tableName);
        Put put = new Put(Bytes.toBytes(strs[2]));// 指定行
        // 参数分别:列族、列、值
        put.add(Bytes.toBytes(strss[0]), Bytes.toBytes(strss[1]),
                Bytes.toBytes(strs[4]));
        table.put(put);
        rsp.appendResult("insert values successful! </br>");
    }

    private void preCheck(String shell, HbaseShellRsp rsp) {
        this.parser = new HShellStringParser(shell, HShellStringParser.SHELL_SEP);
        if (!islegal()) {
            LOG.error("Shell string illegal: {}", shell);
            throw new RuntimeException(tips);
        }

    }

    private boolean islegal() {
        return parser.getHeader().equals("hbase") && (parser.getOpType().equals("-help") || parser
                .getOpType().equals("admin"));
    }

    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                LOG.error("Close admin error", e);
                client = null;
            }
        }
    }
}
