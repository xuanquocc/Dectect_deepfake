import React, {useEffect} from 'react';
import {
  View,
  Button,
  Text,
  NativeModules,
  Platform,
  PermissionsAndroid,
} from 'react-native';

const {BoundServiceModule} = NativeModules;

const App = () => {
  const allow = () => {
    BoundServiceModule.allowPermission();
  };

  const checkApplicationPermissions = async () => {
    if (Platform.OS === 'android') {
      try {
        await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.CAMERA);
        await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS,
        );
        await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
        );
      } catch (error) {
        console.log(error);
      }
    }
  };

  useEffect(() => {
    checkApplicationPermissions();
  }, []);

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Button title="Allow permission" onPress={allow} />
    </View>
  );
};

export default App;
