package soupbinnettytcp.messages;

import java.security.InvalidParameterException;

import soupbinnettytcp.common.Common;

public class LoginRejected extends Message {

    public final char rejectReasonCode;

    public LoginRejected(byte[] bytes) {
        super(bytes);
        rejectReasonCode = Common.toString(bytes, 3).charAt(0);
    }

    public LoginRejected(char rejectReasonCode) {        
        super(Common.padAll(
            'J', 1, Common.PaddingDirection.Right,
            rejectReasonCode, 1, Common.PaddingDirection.Right
        ));
        if(rejectReasonCode != 'A' && rejectReasonCode != 'S')
            throw new InvalidParameterException("rejectReasonCode " + rejectReasonCode + " is not valid, should be 'A' or 'S'");
        this.rejectReasonCode = rejectReasonCode;
    }

}
