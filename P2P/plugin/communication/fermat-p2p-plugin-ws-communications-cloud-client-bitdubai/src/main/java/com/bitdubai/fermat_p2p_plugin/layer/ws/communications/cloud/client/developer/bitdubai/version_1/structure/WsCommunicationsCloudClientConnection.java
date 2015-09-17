/*
 * @#WsCommunicationsCloudClientConnection.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.components.PlatformComponentProfileCommunication;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatPacketCommunicationFactory;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatPacketEncoder;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.client.CommunicationsCloudClientConnection;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.client.CommunicationsVPNConnection;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.components.PlatformComponentProfile;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatMessage;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatPacket;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.AttNamesConstants;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatPacketType;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.PlatformComponentType;
import com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.agents.WsCommunicationsCloudClientAgent;
import com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.agents.WsCommunicationsCloudClientPingAgent;
import com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.processors.CompleteRegistrationComponentPacketProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.processors.RequestListComponentRegisterPacketProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.processors.ServerHandshakeRespondPacketProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.vpn.WsCommunicationVPNClientManagerAgent;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.drafts.Draft_17;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.WsCommunicationsCloudClientConnection</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 02/09/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class WsCommunicationsCloudClientConnection implements CommunicationsCloudClientConnection {

    /**
     * Represent the wsCommunicationsCloudClientChannel
     */
    private WsCommunicationsCloudClientChannel wsCommunicationsCloudClientChannel;

    /**
     * Represent the wsCommunicationVPNClientManagerAgent
     */
    private WsCommunicationVPNClientManagerAgent wsCommunicationVPNClientManagerAgent;

    /**
     * Represent the wsCommunicationsCloudClientAgent
     */
    private WsCommunicationsCloudClientAgent wsCommunicationsCloudClientAgent;

    /**
     * Represent the wsCommunicationsCloudClientPingAgent
     */
    private WsCommunicationsCloudClientPingAgent wsCommunicationsCloudClientPingAgent;

    /**
     * Constructor whit parameters
     *
     * @param uri
     * @param eventManager
     */
    public WsCommunicationsCloudClientConnection(URI uri, EventManager eventManager) {
        super();
        this.wsCommunicationsCloudClientChannel   = WsCommunicationsCloudClientChannel.constructWsCommunicationsCloudClientFactory(uri, new Draft_17(), this, eventManager);
        this.wsCommunicationsCloudClientAgent     = new WsCommunicationsCloudClientAgent(wsCommunicationsCloudClientChannel);
        this.wsCommunicationsCloudClientPingAgent = new WsCommunicationsCloudClientPingAgent(wsCommunicationsCloudClientChannel);
        this.wsCommunicationVPNClientManagerAgent = new WsCommunicationVPNClientManagerAgent();
    }

    /**
     * Register fermat packet processors whit this communication channel
     */
    private void registerFermatPacketProcessors(){

        /*
         * Clean all
         */
        wsCommunicationsCloudClientChannel.cleanPacketProcessorsRegistered();

        /*
         * Register the packet processors
         */
        wsCommunicationsCloudClientChannel.registerFermatPacketProcessor(new ServerHandshakeRespondPacketProcessor());
        wsCommunicationsCloudClientChannel.registerFermatPacketProcessor(new CompleteRegistrationComponentPacketProcessor());
        wsCommunicationsCloudClientChannel.registerFermatPacketProcessor(new RequestListComponentRegisterPacketProcessor());
        //wsCommunicationsCloudClientChannel.registerFermatPacketProcessor(new MessageTransmitPacketProcessor());
    }

    /**
     * Method that initialize the component and open a new connection
     */
    public void initializeAndConnect(){

        /*
         * Register the processors
         */
        registerFermatPacketProcessors();

        /*
         * Start the agent to try the connect
         */
        wsCommunicationsCloudClientAgent.start();


        //wsCommunicationsCloudClientPingAgent.start();

    }


    /**
     * (non-javadoc)
     * @see CommunicationsCloudClientConnection#constructPlatformComponentProfileFactory(String, String, Double, Double, String, NetworkServiceType, PlatformComponentType)
     */
    public PlatformComponentProfile constructPlatformComponentProfileFactory(String identityPublicKey, String alias, Double latitude, Double longitude, String name, NetworkServiceType networkServiceType, PlatformComponentType platformComponentType){

        //Validate parameters
        if ((identityPublicKey == null || identityPublicKey == "") ||
                (alias == null || alias == "")                     ||
                    (name == null || name == "")                   ||
                         latitude == null                          ||
                            longitude == null                      ||
                                networkServiceType == null         ||
                                    platformComponentType == null){

            throw new IllegalArgumentException("All argument are required, can not be null ");

        }


        /*
         * Construct a PlatformComponentProfile instance
         */
        return new PlatformComponentProfileCommunication(alias, wsCommunicationsCloudClientChannel.getClientIdentity().getPublicKey(), identityPublicKey, latitude, longitude, name, networkServiceType, platformComponentType);

    }

    /**
     * (non-javadoc)
     * @see CommunicationsCloudClientConnection#registerComponentInCommunicationCloudServer(PlatformComponentProfile)
     */
    public void registerComponentInCommunicationCloudServer(PlatformComponentProfile platformComponentProfile){

        System.out.println("WsCommunicationsCloudClientConnection - registerComponentInCommunicationCloudServer");

        /*
         * Validate parameter
         */
        if (platformComponentProfile == null){

            throw new IllegalArgumentException("The platformComponentProfile is required, can not be null");
        }

         /*
         * Construct a fermat packet whit the PlatformComponentProfile
         */
        FermatPacket fermatPacketRespond = FermatPacketCommunicationFactory.constructFermatPacketEncryptedAndSinged(wsCommunicationsCloudClientChannel.getServerIdentity(),                  //Destination
                                                                                                                    wsCommunicationsCloudClientChannel.getClientIdentity().getPublicKey(),   //Sender
                                                                                                                    platformComponentProfile.toJson(),                                       //Message Content
                                                                                                                    FermatPacketType.COMPONENT_REGISTRATION_REQUEST,                         //Packet type
                                                                                                                    wsCommunicationsCloudClientChannel.getClientIdentity().getPrivateKey()); //Sender private key

        /*
         * Send the encode packet to the server
         */
        wsCommunicationsCloudClientChannel.send(FermatPacketEncoder.encode(fermatPacketRespond));

    }

    /**
     * (non-javadoc)
     * @see CommunicationsCloudClientConnection#requestListComponentRegistered(PlatformComponentProfile)
     */
    public void requestListComponentRegistered(PlatformComponentProfile requestedPlatformComponentProfile){

        System.out.println("WsCommunicationsCloudClientConnection - requestListComponentRegistered");

        /*
         * Validate parameter
         */
        if (requestedPlatformComponentProfile == null){

            throw new IllegalArgumentException("The platformComponentProfile is required, can not be null");
        }

        /*
         * Get json representation for the filters
         */
        JsonObject messageContent = new JsonObject();
        messageContent.addProperty(AttNamesConstants.JSON_ATT_NAME_COMPONENT_TYPE,       requestedPlatformComponentProfile.getPlatformComponentType().toString());
        messageContent.addProperty(AttNamesConstants.JSON_ATT_NAME_NETWORK_SERVICE_TYPE, requestedPlatformComponentProfile.getNetworkServiceType().toString());

         /*
         * Construct a fermat packet whit the filters
         */
        FermatPacket fermatPacketRespond = FermatPacketCommunicationFactory.constructFermatPacketEncryptedAndSinged(wsCommunicationsCloudClientChannel.getServerIdentity(),                  //Destination
                                                                                                                    wsCommunicationsCloudClientChannel.getClientIdentity().getPublicKey(),   //Sender
                                                                                                                    messageContent.toString(),                                               //Message Content
                                                                                                                    FermatPacketType.REQUEST_LIST_COMPONENT_REGISTERED,                      //Packet type
                                                                                                                    wsCommunicationsCloudClientChannel.getClientIdentity().getPrivateKey()); //Sender private key

        /*
         * Send the encode packet to the server
         */
        wsCommunicationsCloudClientChannel.send(FermatPacketEncoder.encode(fermatPacketRespond));

    }

    /**
     * (non-javadoc)
     * @see CommunicationsCloudClientConnection#requestVpnConnection(PlatformComponentProfile, PlatformComponentProfile)
     */
    public void requestVpnConnection(PlatformComponentProfile applicant, PlatformComponentProfile remoteDestination){


        System.out.println("WsCommunicationsCloudClientConnection - requestListComponentRegistered");

        /*
         * Validate parameter
         */
        if (applicant == null || remoteDestination == null){

            throw new IllegalArgumentException("All parameters are required, can not be null");
        }

        List<PlatformComponentProfile> participants = new ArrayList();
        participants.add(applicant);
        participants.add(remoteDestination);

        /**
         * Validate all are the same type and NETWORK_SERVICE_COMPONENT
         */
        for (PlatformComponentProfile participant: participants) {

            if (participant.getPlatformComponentType() != PlatformComponentType.NETWORK_SERVICE_COMPONENT){
                throw new IllegalArgumentException("All the PlatformComponentProfile has to be NETWORK_SERVICE_COMPONENT ");
            }

            if (participant.getNetworkServiceType() != applicant.getNetworkServiceType()){
                throw new IllegalArgumentException("All the PlatformComponentProfile has to be the same type of network service type ");
            }
        }

        /*
         * Construct the json object
         */
        Gson gson = new Gson();

        /*
         * Convert to json representation
         */
        String jsonListRepresentation = gson.toJson(participants, new TypeToken<List<PlatformComponentProfileCommunication>>() { }.getType());

         /*
         * Construct a fermat packet whit the request
         */
        FermatPacket fermatPacketRespond = FermatPacketCommunicationFactory.constructFermatPacketEncryptedAndSinged(wsCommunicationsCloudClientChannel.getServerIdentity(),                  //Destination
                                                                                                                    wsCommunicationsCloudClientChannel.getClientIdentity().getPublicKey(),   //Sender
                                                                                                                    jsonListRepresentation,                                                  //Message Content
                                                                                                                    FermatPacketType.COMPONENT_CONNECTION_REQUEST,                           //Packet type
                                                                                                                    wsCommunicationsCloudClientChannel.getClientIdentity().getPrivateKey()); //Sender private key
        /*
         * Send the encode packet to the server
         */
        wsCommunicationsCloudClientChannel.send(FermatPacketEncoder.encode(fermatPacketRespond));

        /*
         * Add the applicant to the requested list
         */
        wsCommunicationVPNClientManagerAgent.addRequestedVpnConnections(applicant, remoteDestination);

    }

    /**
     * (non-javadoc)
     * @see CommunicationsCloudClientConnection#isConnected()
     */
    public boolean isConnected(){
        return wsCommunicationsCloudClientChannel.getConnection().isOpen();
    }

    /**
     * (non-javadoc)
     * @see CommunicationsCloudClientConnection#getCommunicationsVPNConnectionStablished(PlatformComponentProfile, String)
     */
    @Override
    public CommunicationsVPNConnection getCommunicationsVPNConnectionStablished(PlatformComponentProfile applicant, String remotePlatformComponentProfile) {

        if (applicant.getPlatformComponentType() != PlatformComponentType.NETWORK_SERVICE_COMPONENT){
            throw new IllegalArgumentException("All the PlatformComponentProfile has to be NETWORK_SERVICE_COMPONENT ");
        }

        if (applicant.getNetworkServiceType() != applicant.getNetworkServiceType()){
            throw new IllegalArgumentException("All the PlatformComponentProfile has to be the same type of network service type ");
        }

        return wsCommunicationVPNClientManagerAgent.getActiveVpnConnection(applicant, remotePlatformComponentProfile);
    }

    /**
     * Get the WsCommunicationsCloudClientChannel
     * @return WsCommunicationsCloudClientChannel
     */
    public WsCommunicationsCloudClientChannel getWsCommunicationsCloudClientChannel() {
        return wsCommunicationsCloudClientChannel;
    }

    /**
     * Get the WsCommunicationVPNClientManagerAgent
     * @return WsCommunicationVPNClientManagerAgent
     */
    public WsCommunicationVPNClientManagerAgent getWsCommunicationVPNClientManagerAgent() {
        return wsCommunicationVPNClientManagerAgent;
    }


    /**
     * (non-javadoc)
     * @see CommunicationsCloudClientConnection#isRegister()
     */
    public boolean isRegister() {
        return wsCommunicationsCloudClientChannel.isRegister();
    }
}