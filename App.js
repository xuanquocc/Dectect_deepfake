import React, {useState, useEffect} from 'react';
import {View, Text, Button, NativeModules} from 'react-native';

const {ScreenRecordModule, CountingServiceModule} = NativeModules;

const App = () => {
  let buttonDisabled = false;
  const start = () => {
    CountingServiceModule.startMusic();
  };

  const stop = () => {
    CountingServiceModule.stopMusic();
  };
  const allow = () => {
    CountingServiceModule.allowPermission();
  };

  const startRecording = () => {
    ScreenRecordModule.startScreenRecording();
  };

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Button title="Start music" onPress={start} />
      <Button title="Stop music" onPress={stop} />
      <Button title="Allow permission" onPress={allow} />
      <Button title="Start Recording" onPress={startRecording} />
    </View>
  );
};

export default App;
