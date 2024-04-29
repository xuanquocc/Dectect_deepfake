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

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Button
        title={isRecording ? 'Stop Recording' : 'Start Recording'}
        onPress={isRecording ? stopRecording : startRecording}
      />
    </View>
  );
};

export default App;
