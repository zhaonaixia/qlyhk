package com.cd.qlyhk.common.vo;

import com.cd.rdf.base.BaseValueObject;

/**
 * @author zhangchaojie
 * @version 1.00.00
 * @date 2018-07-19 11:07
 * 清单列表统计结果类
 */
public class BillCount extends BaseValueObject{
    
    private static final long serialVersionUID = 4869754946195836392L;
    
    //已生成凭证清单数目
    private int voucherNum;
    //未生成凭证清单数目
    private int novoucherNum;
    //清单条数
    private int billNum;
    //附件张数
    private int attachmentNum;
    //正金额
    private double postiveExTaxAmount;
    //负金额
    private double negativeExTaxAmount;
    //正税额
    private double positiveTax;
    //负税额
    private double negativeTax;
    //正价税合计
    private double positiveAmount;
    //负价税合计
    private double negativeAmount;
    
    //数据库中实际清单条数
    private int billNumInDB;

    public int getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(int voucherNum) {
        this.voucherNum = voucherNum;
    }

    public int getNovoucherNum() {
        return novoucherNum;
    }

    public void setNovoucherNum(int novoucherNum) {
        this.novoucherNum = novoucherNum;
    }

    public int getBillNum() {
        return billNum;
    }

    public void setBillNum(int billNum) {
        this.billNum = billNum;
    }

    public int getAttachmentNum() {
        return attachmentNum;
    }

    public void setAttachmentNum(int attachmentNum) {
        this.attachmentNum = attachmentNum;
    }

    public double getPostiveExTaxAmount() {
        return postiveExTaxAmount;
    }

    public void setPostiveExTaxAmount(double postiveExTaxAmount) {
        this.postiveExTaxAmount = postiveExTaxAmount;
    }

    public double getNegativeExTaxAmount() {
        return negativeExTaxAmount;
    }

    public void setNegativeExTaxAmount(double negativeExTaxAmount) {
        this.negativeExTaxAmount = negativeExTaxAmount;
    }

    public double getPositiveTax() {
        return positiveTax;
    }

    public void setPositiveTax(double positiveTax) {
        this.positiveTax = positiveTax;
    }

    public double getNegativeTax() {
        return negativeTax;
    }

    public void setNegativeTax(double negativeTax) {
        this.negativeTax = negativeTax;
    }

    public double getPositiveAmount() {
        return positiveAmount;
    }

    public void setPositiveAmount(double positiveAmount) {
        this.positiveAmount = positiveAmount;
    }

    public double getNegativeAmount() {
        return negativeAmount;
    }

    public void setNegativeAmount(double negativeAmount) {
        this.negativeAmount = negativeAmount;
    }

    public int getBillNumInDB() {
        return billNumInDB;
    }

    public void setBillNumInDB(int billNumInDB) {
        this.billNumInDB = billNumInDB;
    }
    
}
