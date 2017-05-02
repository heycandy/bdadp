package com.chinasofti.ark.bdadp.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by White on 2016/08/30.
 */
@Entity
public class UserGroup {

    @Id
    private String groupId;
    private String groupName;
    private String groupDesc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @OneToOne
    @JoinColumn(name = "createUser")
    private User createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifiedTime;

    @OneToOne
    @JoinColumn(name = "modifiedUser")
    private User modifiedUser;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "UserGroupJoinUser", joinColumns = {
            @JoinColumn(name = "groupId")}, inverseJoinColumns = {@JoinColumn(name = "userId")})
    private List<User> users;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public User getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(User modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupDesc='" + groupDesc + '\'' +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", modifiedTime=" + modifiedTime +
                ", modifiedUser=" + modifiedUser +
                ", users=" + users +
                '}';
    }
}
