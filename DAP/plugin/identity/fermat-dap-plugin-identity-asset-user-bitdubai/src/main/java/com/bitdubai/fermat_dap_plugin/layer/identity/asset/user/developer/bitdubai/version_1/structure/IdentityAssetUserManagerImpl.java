package com.bitdubai.fermat_dap_plugin.layer.identity.asset.user.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.DealsWithPluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_api.layer.pip_Identity.developer.exceptions.CantCreateNewDeveloperException;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_issuer.exceptions.CantCreateNewIdentityAssetIssuerException;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_user.exceptions.CantCreateNewIdentityAssetUserException;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_user.exceptions.CantListAssetUsersException;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_user.interfaces.IdentityAssetUser;
import com.bitdubai.fermat_dap_plugin.layer.identity.asset.user.developer.bitdubai.version_1.database.AssetUserIdentityDao;
import com.bitdubai.fermat_dap_plugin.layer.identity.asset.user.developer.bitdubai.version_1.exceptions.CantInitializeAssetUserIdentityDatabaseException;
import com.bitdubai.fermat_dap_plugin.layer.identity.asset.user.developer.bitdubai.version_1.exceptions.CantListAssetUserIdentitiesException;
import com.bitdubai.fermat_pip_api.layer.pip_user.device_user.exceptions.CantGetLoggedInDeviceUserException;
import com.bitdubai.fermat_pip_api.layer.pip_user.device_user.interfaces.DeviceUser;
import com.bitdubai.fermat_pip_api.layer.pip_user.device_user.interfaces.DeviceUserManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.ErrorManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by franklin on 02/11/15.
 */
public class IdentityAssetUserManagerImpl implements DealsWithErrors, DealsWithLogger, DealsWithPluginDatabaseSystem, DealsWithPluginFileSystem {
    /**
     * IdentityAssetIssuerManagerImpl member variables
     */
    UUID pluginId;

    /**
     * DealsWithErrors interface member variables
     */
    ErrorManager errorManager;

    /**
     * DealsWithLogger interface mmeber variables
     */
    LogManager logManager;

    /**
     * DealsWithPluginDatabaseSystem interface member variables
     */
    PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * DealsWithPluginFileSystem interface member variables
     */
    PluginFileSystem pluginFileSystem;


    /**
     * DealsWithDeviceUsers Interface member variables.
     */
    private DeviceUserManager deviceUserManager;

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    @Override
    public void setPluginFileSystem(PluginFileSystem pluginFileSystem) {
        this.pluginFileSystem = pluginFileSystem;
    }

    /**
     * Constructor
     *
     * @param errorManager
     * @param logManager
     * @param pluginDatabaseSystem
     * @param pluginFileSystem
     */
    public IdentityAssetUserManagerImpl(ErrorManager errorManager, LogManager logManager, PluginDatabaseSystem pluginDatabaseSystem, PluginFileSystem pluginFileSystem, UUID pluginId, DeviceUserManager deviceUserManager) {
        this.errorManager = errorManager;
        this.logManager = logManager;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
        this.deviceUserManager = deviceUserManager;
    }

    private AssetUserIdentityDao getAssetUserIdentityDao(){
        AssetUserIdentityDao assetUserIdentityDao = new AssetUserIdentityDao(this.pluginDatabaseSystem, this.pluginFileSystem, this.pluginId);
        return assetUserIdentityDao;
    }

    public void  initializeDatabase() throws CantInitializeAssetUserIdentityDatabaseException {
        getAssetUserIdentityDao().initializeDatabase();
    }

    public List<IdentityAssetUser> getIdentityAssetUsersFromCurrentDeviceUser() throws CantListAssetUsersException {

        try {

            List<IdentityAssetUser> assetUserList = new ArrayList<IdentityAssetUser>();


            DeviceUser loggedUser = deviceUserManager.getLoggedInDeviceUser();
            assetUserList = getAssetUserIdentityDao().getAllIntraUserFromCurrentDeviceUser(loggedUser);


            return assetUserList;

        } catch (CantGetLoggedInDeviceUserException e) {
            throw new CantListAssetUsersException("CAN'T GET ASSET ISSUER IDENTITIES", e, "Error get logged user device", "");
        } catch (CantListAssetUserIdentitiesException e) {
            throw new CantListAssetUsersException("CAN'T GET ASSET ISSUER  IDENTITIES", e, "", "");
        } catch (Exception e) {
            throw new CantListAssetUsersException("CAN'T GET ASSET ISSUER IDENTITIES", FermatException.wrapException(e), "", "");
        }
    }

    public IdentityAssetUser createNewIdentityAssetUser(String alias, byte[] profileImage) throws CantCreateNewIdentityAssetUserException {
        try {
            DeviceUser loggedUser = deviceUserManager.getLoggedInDeviceUser();

            ECCKeyPair keyPair = new ECCKeyPair();
            String publicKey = keyPair.getPublicKey();
            String privateKey = keyPair.getPrivateKey();

            getAssetUserIdentityDao().createNewUser(alias, publicKey, privateKey, loggedUser, profileImage);

            IdentityAssetUsermpl identityAssetUser = new IdentityAssetUsermpl(alias, publicKey, privateKey, profileImage, pluginFileSystem, pluginId);

            //TODO:Revisar como registrar con el Network Service
            //registerIdentities();

            return identityAssetUser;
        } catch (CantGetLoggedInDeviceUserException e) {
            throw new CantCreateNewIdentityAssetUserException("CAN'T CREATE NEW ASSET USER IDENTITY", e, "Error getting current logged in device user", "");
        } catch (CantCreateNewDeveloperException e) {
            throw new CantCreateNewIdentityAssetUserException("CAN'T CREATE NEW ASSET USER IDENTITY", e, "Error save user on database", "");
        } catch (Exception e) {
            throw new CantCreateNewIdentityAssetUserException("CAN'T CREATE NEW ASSET USER IDENTITY", FermatException.wrapException(e), "", "");
        }
    }

    public boolean  hasIntraUserIdentity() throws CantListAssetUsersException{
        try {

            DeviceUser loggedUser = deviceUserManager.getLoggedInDeviceUser();
            if(getAssetUserIdentityDao().getAllIntraUserFromCurrentDeviceUser(loggedUser).size() > 0)
                return true;
            else
                return false;
        } catch (CantGetLoggedInDeviceUserException e) {
            throw new CantListAssetUsersException("CAN'T GET IF ASSET USER IDENTITIES  EXISTS", e, "Error get logged user device", "");
        }  catch (CantListAssetUserIdentitiesException e) {
            throw new CantListAssetUsersException("CAN'T GET IF ASSET USER IDENTITIES EXISTS", e, "", "");
        }catch (Exception e) {
            throw new CantListAssetUsersException("CAN'T GET ASSET USER USER IDENTITY EXISTS", FermatException.wrapException(e), "", "");
        }
    }

    public void registerIdentities(){
//        try {
//            List<IntraWalletUser> lstIntraWalletUSer = intraWalletUserIdentityDao.getAllIntraUserFromCurrentDeviceUser(deviceUserManager.getLoggedInDeviceUser());
//            List<Actor> lstActors = new ArrayList<Actor>();
//            for(IntraWalletUser user : lstIntraWalletUSer){
//                lstActors.add(intraActorManager.contructIdentity(user.getPublicKey(), user.getAlias(), Actors.INTRA_USER,user.getProfileImage()));
//            }
//            intraActorManager.registrateActors(lstActors);
//        } catch (CantListIntraWalletUserIdentitiesException e) {
//            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_CCP_INTRA_WALLET_USER_IDENTITY, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
//        } catch (CantGetLoggedInDeviceUserException e) {
//            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_CCP_INTRA_WALLET_USER_IDENTITY, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
//        }
    }
}