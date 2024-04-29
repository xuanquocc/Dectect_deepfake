import React, {useState, useEffect} from 'react';
import {View, Text, Button} from 'react-native';
import {NativeModules} from 'react-native';

const {ScreenRecordModule} = NativeModules;

const App = () => {
  const [isRecording, setIsRecording] = useState(false);
  const startRecording = () => {
    setIsRecording(true);
    ScreenRecordModule.startScreenRecording();
  };

  const stopRecording = () => {
    setIsRecording(false);
    ScreenRecordModule.stopScreenRecording();
  };
  const startCounting = () => {
    CountingService.startCounting();
  };

  const stopCounting = () => {
    CountingService.stopCounting();
  };
  const allow = () => {
    CountingService.allowPermission();
  };
  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Button title="Start music" onPress={startCounting} />
      <Button title="Stop music" onPress={stopCounting} />
      <Button title="Allow permission" onPress={allow} />
      <Button
        title={isRecording ? 'Stop Recording' : 'Start Recording'}
        onPress={isRecording ? stopRecording : startRecording}
      />
    </View>
  );
};

export default App;
