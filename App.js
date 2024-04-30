import React, {useState, useEffect} from 'react';
import {View, Text, Button, NativeModules} from 'react-native';

const {ScreenRecordModule, CountingServiceModule} = NativeModules;

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
  const start = () => {
    CountingServiceModule.startMusic();
  };

  const stop = () => {
    CountingServiceModule.stopMusic();
  };
  const allow = () => {
    CountingServiceModule.allowPermission();
  };
  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Button title="Start music" onPress={start} />
      <Button title="Stop music" onPress={stop} />
      <Button title="Allow permission" onPress={allow} />
      <Button
        title={isRecording ? 'Stop Recording' : 'Start Recording'}
        onPress={isRecording ? stopRecording : startRecording}
      />
    </View>
  );
};

export default App;
