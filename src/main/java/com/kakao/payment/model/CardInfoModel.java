package com.kakao.payment.model;

import com.kakao.payment.service.EncryptionService;
import com.kakao.payment.service.impl.EncryptionServiceImpl;
import lombok.Getter;

public class CardInfoModel {
    private final EncryptionService encryptionService = new EncryptionServiceImpl();

    @Getter
    private final String cardNum;
    @Getter
    private final String cvc;
    @Getter
    private final String exp;

    public CardInfoModel(String cardNum, short exp, short cvc) {
        this.cardNum = cardNum;
        this.exp = Short.toString(exp);
        this.cvc = Short.toString(cvc);
    }

    public CardInfoModel(String cardInfoStr) {
        String cardInfo = encryptionService.decrypt(cardInfoStr);
        String[] cardInfos = cardInfo.split("[|]");

        this.cardNum = cardInfos[0];
        this.exp = cardInfos[1];
        this.cvc = cardInfos[2];
    }

    public String getEncStr() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.cardNum)
                .append("|")
                .append(this.exp)
                .append("|")
                .append(this.cvc);

        return encryptionService.encrypt(sb.toString());
    }

    public String getEncCardNum() {
        StringBuilder sb = new StringBuilder(this.cardNum);
        // hide some numbers
        for (int i = 7; i < sb.length() - 3; i++)
            sb.setCharAt(i, '*');

        return sb.toString();
    }
}
