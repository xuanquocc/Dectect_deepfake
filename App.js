import React, {useState, useEffect} from 'react';
import {View, Text, Button, NativeModules} from 'react-native';

const {ScreenRecordModule, BoundServiceModule, CallDetectionModule} = NativeModules;

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

  const startDetected =  () => {
    CallDetectionModule.startCallDetectionService();
  };
  const start = () => {
    BoundServiceModule.startMusic();
  };

  const stop = () => {
    BoundServiceModule.stopMusic();
  };
  const allow = () => {
    BoundServiceModule.allowPermission();
  };

  useEffect(() => {
    startDetected()
  }, []);
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
