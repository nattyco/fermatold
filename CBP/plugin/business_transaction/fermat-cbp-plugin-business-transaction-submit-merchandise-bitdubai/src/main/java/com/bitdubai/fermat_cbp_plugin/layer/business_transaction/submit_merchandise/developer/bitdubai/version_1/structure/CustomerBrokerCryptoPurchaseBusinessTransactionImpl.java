package com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_broker_crypto_purchase.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_cbp_api.all_definition.business_transaction.BusinessTransaction;
import com.bitdubai.fermat_cbp_api.all_definition.enums.BusinessTransactionStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.CryptoCurrencyType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.CurrencyType;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.customer_broke_crypto_purchase.interfaces.CustomerBrokerCryptoPurchase;

import java.util.UUID;

/**
 * Created by Yordin Alayn on 29.09.15.
 */
public class CustomerBrokerCryptoPurchaseBusinessTransactionImpl implements BusinessTransaction, CustomerBrokerCryptoPurchase{

    private final UUID transactionId;
    private final UUID contractId;
    private final String publicKeyBroker;
    private final String publicKeyCustomer;
    private final UUID paymentTransactionId;
    private final CurrencyType paymentCurrency;
    private final CurrencyType merchandiseCurrency;
    private final float merchandiseAmount;
    private final UUID executionTransactionId;
    private final CryptoCurrencyType cryptoCurrencyType;
    private final BusinessTransactionStatus transactionStatus;

    public CustomerBrokerCryptoPurchaseBusinessTransactionImpl(
            UUID transactionId,
            UUID contractId,
            String publicKeyBroker,
            String publicKeyCustomer,
            UUID paymentTransactionId,
            CurrencyType paymentCurrency,
            CurrencyType merchandiseCurrency,
            float merchandiseAmount,
            UUID executionTransactionId,
            CryptoCurrencyType cryptoCurrencyType,
            BusinessTransactionStatus transactionStatus
    ){
        this.transactionId = transactionId;
        this.contractId = contractId;
        this.publicKeyBroker = publicKeyBroker;
        this.publicKeyCustomer = publicKeyCustomer;
        this.paymentTransactionId = paymentTransactionId;
        this.paymentCurrency = paymentCurrency;
        this.merchandiseCurrency = merchandiseCurrency;
        this.merchandiseAmount = merchandiseAmount;
        this.executionTransactionId = executionTransactionId;
        this.cryptoCurrencyType = cryptoCurrencyType;
        this.transactionStatus = transactionStatus;
    }

    @Override
    public UUID getTransactionId(){ return this.transactionId; }

    @Override
    public UUID getContractId(){ return this.contractId; }

    @Override
    public String getPublicKeyBroker(){ return this.publicKeyBroker; }

    @Override
    public String getPublicKeyCustomer(){ return this.publicKeyCustomer; }

    @Override
    public UUID getPaymentTransactionId(){ return this.paymentTransactionId; }

    @Override
    public CurrencyType getPaymentCurrency(){ return this.paymentCurrency; }

    @Override
    public CurrencyType getMerchandiseCurrency(){ return this.merchandiseCurrency; }

    @Override
    public float getMerchandiseAmount(){ return this.merchandiseAmount; }

    @Override
    public UUID getExecutionTransactionId(){ return this.executionTransactionId; }

    @Override
    public CryptoCurrencyType getCryptoCurrencyType(){ return this.cryptoCurrencyType; }

    @Override
    public BusinessTransactionStatus getStatus(){ return this.transactionStatus; }

}
