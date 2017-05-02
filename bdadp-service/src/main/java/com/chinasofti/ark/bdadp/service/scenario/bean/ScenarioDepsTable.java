package com.chinasofti.ark.bdadp.service.scenario.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * Created by White on 2016/11/19.
 */
public class ScenarioDepsTable implements Serializable {

    private String previewVersion;
    private String archiverVersion;
    private String entryEncoding;
    private String builtBy;
    private String createBy;
    private String buildJdk;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Collection<ScenarioDepsRow> previewRows;

    public String getPreviewVersion() {
        return previewVersion;
    }

    public ScenarioDepsTable setPreviewVersion(String previewVersion) {
        this.previewVersion = previewVersion;
        return this;
    }

    public String getArchiverVersion() {
        return archiverVersion;
    }

    public ScenarioDepsTable setArchiverVersion(String archiverVersion) {
        this.archiverVersion = archiverVersion;
        return this;
    }

    public String getEntryEncoding() {
        return entryEncoding;
    }

    public ScenarioDepsTable setEntryEncoding(String entryEncoding) {
        this.entryEncoding = entryEncoding;
        return this;
    }

    public String getBuiltBy() {
        return builtBy;
    }

    public ScenarioDepsTable setBuiltBy(String builtBy) {
        this.builtBy = builtBy;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public ScenarioDepsTable setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public String getBuildJdk() {
        return buildJdk;
    }

    public ScenarioDepsTable setBuildJdk(String buildJdk) {
        this.buildJdk = buildJdk;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ScenarioDepsTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Collection<ScenarioDepsRow> getPreviewRows() {
        return previewRows;
    }

    public ScenarioDepsTable setPreviewRows(
            Collection<ScenarioDepsRow> previewRows) {
        this.previewRows = previewRows;
        return this;
    }
}
