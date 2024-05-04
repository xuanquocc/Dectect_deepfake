import React from 'react';
import {View, Button, NativeModules} from 'react-native';

const {BoundServiceModule} = NativeModules;

const App = () => {
  const allow = () => {
    BoundServiceModule.allowPermission();
  };

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Button title="Allow permission" onPress={allow} />
    </View>
  );
};

export default App;
