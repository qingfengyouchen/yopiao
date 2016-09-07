package com.zx.stlife.entity.member;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.zx.stlife.base.exception.BizException;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.SuperIntVersion;
import com.zx.stlife.entity.sys.User;

/**
 * 会员信息
 */
@Entity
@Table(name = "sys_member")
public class Member extends SuperIntVersion {

    /**
     * Default constructor
     */
    public Member() {
    }

    public Member(String trueName, Integer balance, Integer jifen,
                  MemberLevel memberLevel, User user, Boolean canSpeak, Boolean canSnatch,Integer type, String accountName,String accountCode,String accountBank,Integer qq) {
        super(Const.CommonState.ENABLE);
        this.trueName = trueName;
        this.balance = balance;
        this.jifen = jifen;
        this.memberLevel = memberLevel;
        this.user = user;
        this.canSpeak = canSpeak;
        this.canSnatch = canSnatch;
        this.type=type;
        this.accountName=accountName;
        this.accountCode=accountCode;
        this.accountBank=accountBank;
//        this.qq=qq;
    }

    /**
     * 真实姓名
     */
    private String trueName;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 余额
     */
    private Integer balance = 0;

    /**
     * 积分
     */
    private Integer jifen = 0;

    /**
     * 会员等级
     */
    private MemberLevel memberLevel;

    /**
     * 
     */
    private Boolean canSpeak;

    /**
     * 
     */
    private Boolean canSnatch = true;

    /**
     * 
     */
    private Date editTime;

    /**
     * 
     */
    
    private Integer type;
    /**
     * 支付宝姓名
     */
    private String accountName;
    
    /**
     * 支付宝账户
     */
    private String accountCode;
    
    /**
     * 
     */
    private String accountBank;

    /**
     * 
     */
    private long qq;
    
    /**
     *
     */
    private User user;
    
    //private RechargeRecord rechargeRecord;

    /**
     * 可领的推荐码红包（推荐别人注册后获取的红包）
     */
    private Integer prizeAmount = 0;


    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getJifen() {
        return jifen;
    }

    public void setJifen(Integer jifen) {
        this.jifen = jifen;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "member_level_id")
    public MemberLevel getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(MemberLevel memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Boolean getCanSpeak() {
        return canSpeak;
    }

    public void setCanSpeak(Boolean canSpeak) {
        this.canSpeak = canSpeak;
    }

    public Boolean getCanSnatch() {
        return canSnatch;
    }

    public void setCanSnatch(Boolean canSnatch) {
        this.canSnatch = canSnatch;
    }

  

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public long getQq() {
		return qq;
	}

	public void setQq(long qq) {
		this.qq = qq;
	}

	public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Integer getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(Integer prizeAmount) {
        this.prizeAmount = prizeAmount;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    /*
    public RechargeRecord getRechargeRecord() {
        return rechargeRecord;
    }

    public void setRechargeRecord(RechargeRecord rechargeRecord) {
        this.rechargeRecord = rechargeRecord;
    }
    */

    public int addBalance(int money){
        if(money > 0) {
            money = (getBalance() == null ? 0 : getBalance()) + money;
            setBalance(money);
        }
        return getBalance();
    }

    public int subtractBalance(int money){
        money = (getBalance() == null ? 0 : getBalance()) - money;
        if(money < 0){
            throw new BizException("余额不足");
        }
        setBalance(money);
        return money;
    }

    public int addJifen(int jifen){
        if(jifen > 0) {
            jifen = (getJifen() == null ? 0 : getJifen()) + jifen;
            setJifen(jifen);
        }
        return getJifen();
    }

    public int subtractJifen(int jifen){
        jifen = (getJifen() == null ? 0 : getJifen()) - jifen;
        if(jifen < 0){
            throw new BizException("积分不足");
        }
        setJifen(jifen);
        return jifen;
    }
}