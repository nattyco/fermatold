package com.bitdubai.fermat_ccp_api.layer.request.crypto_payment.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_ccp_api.layer.request.crypto_payment.enums.CryptoPaymentRequestState;
import com.bitdubai.fermat_ccp_api.layer.request.crypto_payment.exceptions.CantApproveCryptoPaymentRequestException;
import com.bitdubai.fermat_ccp_api.layer.request.crypto_payment.exceptions.CantListCryptoPaymentRequestsException;
import com.bitdubai.fermat_ccp_api.layer.request.crypto_payment.exceptions.CantRejectCryptoPaymentRequestException;
import com.bitdubai.fermat_ccp_api.layer.request.crypto_payment.exceptions.CantSendCryptoPaymentRequestException;
import com.bitdubai.fermat_ccp_api.layer.request.crypto_payment.exceptions.CryptoPaymentRequestNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * The interface <code>com.bitdubai.fermat_ccp_api.layer.request.crypto_payment.interfaces.CryptoPaymentManager</code>
 * provide the methods to negotiate crypto payment requests.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 30/09/2015.
 */
public interface CryptoPaymentManager {

    /**
     * Throw the method <code>sendCryptoPaymentRequest</code> you can send a crypto payment request to the actor with
     * public key receiverActorPublicKey the actors are always intra-wallet-user of CCP.
     *
     * @param walletPublicKey     public key of the wallet sending the request.
     * @param identityPublicKey   public key of the identity sending the request.
     * @param cryptoAddress       crypto address where the identity wants to receive the payment.
     * @param actorPublicKey      public key of the actor whom will receive the request.
     * @param description         text describing the crypto payment request.
     * @param amount              amount of crypto expected.
     *
     * @throws CantSendCryptoPaymentRequestException if something goes wrong.
     */
    void sendCryptoPaymentRequest(String        walletPublicKey  ,
                                  String        identityPublicKey,
                                  CryptoAddress cryptoAddress    ,
                                  String        actorPublicKey   ,
                                  String        description      ,
                                  long          amount           ) throws CantSendCryptoPaymentRequestException;

    /**
     * Throw the method <code>refuseRequest</code> you can inform the rejection of a request to its requester.
     *
     * @param requestId  uuid identifying the request to reject.
     *
     * @throws CantRejectCryptoPaymentRequestException   if something goes wrong.
     * @throws CryptoPaymentRequestNotFoundException     if we can't find the payment request.
     */
    void refuseRequest(UUID requestId) throws CantRejectCryptoPaymentRequestException,
                                              CryptoPaymentRequestNotFoundException  ;

    /**
     * Throw the method <code>approveRequest</code> you can approve a request to its requester and send crypto.
     *
     * @param requestId  uuid identifying the request to approve.
     *
     * @throws CantApproveCryptoPaymentRequestException   if something goes wrong.
     * @throws CryptoPaymentRequestNotFoundException      if we can't find the payment request.
     */
    void approveRequest(UUID requestId) throws CantApproveCryptoPaymentRequestException,
                                               CryptoPaymentRequestNotFoundException   ;

    /**
     * Throw the method <code>listCryptoPaymentRequests</code> you can get the list of the requests related with a wallet.
     *
     * @param walletPublicKey  public key of the wallet who needs to list the crypto payment requests.
     * @param max              quantity of CryptoPaymentRequests you want to return.
     * @param offset           the point of start in the list that you're trying to bring.
     *
     * @return the list of requests.
     *
     * @throws CantListCryptoPaymentRequestsException if something goes wrong.
     */
    List<CryptoPaymentRequest> listCryptoPaymentRequests(String  walletPublicKey,
                                                         Integer max            ,
                                                         Integer offset         ) throws CantListCryptoPaymentRequestsException;

    /**
     * Throw the method <code>listCryptoPaymentRequests</code> you can get the list of the requests related with a wallet.
     *
     * @param walletPublicKey  public key of the wallet who needs to list the crypto payment requests.
     * @param state            element of CryptoPaymentRequestState indicating the state of the requests that we need.
     * @param max              quantity of CryptoPaymentRequests you want to return.
     * @param offset           the point of start in the list that you're trying to bring.
     *
     * @return the list of requests.
     *
     * @throws CantListCryptoPaymentRequestsException if something goes wrong.
     */
    List<CryptoPaymentRequest> listCryptoPaymentRequestsByState(String                    walletPublicKey,
                                                                CryptoPaymentRequestState state          ,
                                                                Integer                   max            ,
                                                                Integer                   offset         ) throws CantListCryptoPaymentRequestsException;
}