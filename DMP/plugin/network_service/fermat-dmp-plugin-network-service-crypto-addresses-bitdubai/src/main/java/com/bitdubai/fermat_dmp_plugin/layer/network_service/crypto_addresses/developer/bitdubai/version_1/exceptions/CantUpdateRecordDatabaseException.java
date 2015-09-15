package com.bitdubai.fermat_dmp_plugin.layer.network_service.crypto_addresses.developer.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by rodrigo on 8/11/15.
 */
public class CantUpdateRecordDatabaseException extends FermatException {
    public final static String DEFAULT_MESSAGE = "There was an error trying to update a database record.";

    public CantUpdateRecordDatabaseException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}
