/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['mcs'], function () {
  var mcs_config = {
    'logLevel': mcs.logLevelInfo,
    'mobileBackends': {
      'CCDWorkshop': {
        'default': true,
        'baseUrl': 'https://mcs-jporacletrial70175.mobileenv.us2.oraclecloud.com:443',
        'authorization': {
          'basicAuth': {
            'backendId': 'da9590be-6540-4f91-9d8f-cd7d802f883b',
            'anonymousToken': 'SlBPUkFDTEVUUklBTDcwMTc1X01DU19NT0JJTEVfQU5PTllNT1VTX0FQUElEOnFtamw1N3B5d3pfWGFj'
          },
          'oAuth': {
            'clientId': 'bae93b32-2b58-4ea4-809f-848027912040',
            'clientSecret': 'XZFgxjJkBRnJegprcLj2',
            'tokenEndpoint': 'https://jporacletrial70175.identity.us.oraclecloud.com/oam/oauth2/tokens'
          }
        }
      }
    }
  };
  
  function MobileBackend() {
    var self = this;
    self.mobileBackend;
    
    // ログイン処理
    self.login = function (username, password,
            successCallback, errorCallback) {
    self.mobileBackend.Authorization.authenticate(username, password,
            successCallback, errorCallback);
    };

    // ログアウト処理
    self.logout = function () {
        self.mobileBackend.Authorization.logout();
    };

    self.invokeGetIncidentsStats = function (technician, 
            successCallback, errorCallback) {
    self.mobileBackend.CustomCode.invokeCustomCodeJSONRequest(
            'ifixitfast/stats/incidents?technician=' + technician,
            'GET', null, successCallback, errorCallback);
    };
    
    function init() {
      mcs.MobileBackendManager.setConfig(mcs_config);
      self.mobileBackend =
          mcs.MobileBackendManager.getMobileBackend('CCDWorkshop');
      self.mobileBackend.setAuthenticationType('basicAuth');
    }
    
    init();
  }
  return new MobileBackend();
});
