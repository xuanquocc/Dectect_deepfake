import React, {useState, useEffect} from 'react';
import {View, Text, Button, PermissionsAndroid} from 'react-native';
import CountingService from './src/modules/CountingServiceModule';

import RecordScreen, {RecordingResult} from 'react-native-record-screen';

const App = () => {
  const [count, setCount] = useState(0);
  const [isCounting, setIsCounting] = useState(false);

  useEffect(() => {
    const interval = setInterval(() => {
      if (isCounting) {
        setCount(prevCount => prevCount + 1);
      }
    }, 1000);

    return () => clearInterval(interval);
  }, [isCounting]);

  const startCounting = () => {
    setIsCounting(true);
    CountingService.startCounting();
  };

  const stopCounting = () => {
    setIsCounting(false);
    CountingService.stopCounting();
  };
  const allow = () => {
    CountingService.allowPermission();
  };

  const [isRecording, setIsRecording] = useState(false);

  const startRecording = async () => {
    try {
      const granted = await PermissionsAndroid.requestMultiple(
        [PermissionsAndroid.PERMISSIONS.RECORD_AUDIO],
        {
          title: 'Screen Recording Permission',
          message: 'Do you want to record with audio?',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        },
      );

      if (
        granted[PermissionsAndroid.PERMISSIONS.RECORD_AUDIO] ===
        PermissionsAndroid.RESULTS.GRANTED
      ) {
        setIsRecording(true);
        RecordScreen.startRecording().catch(error => console.error(error));
      } else {
        setIsRecording(true);
        RecordScreen.startRecording({mic: false}).catch(error =>
          console.error(error),
        );
      }
    } catch (err) {
      console.warn(err);
    }
  };

  const stopRecording = async () => {
    // recording stop
    const res = await RecordScreen.stopRecording().catch(error =>
      console.warn(error),
    );
    if (res) {
      const url = res.result.outputURL;
    }
  };

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Text style={{fontSize: 24, marginBottom: 20}}>Count: {count}</Text>
      <Button title="Start" onPress={startCounting} disabled={isCounting} />
      <Button title="Stop" onPress={stopCounting} disabled={!isCounting} />
      <Button title="Allow permission" onPress={allow} />
      <Button
        title={isRecording ? 'Stop Recording' : 'Start Recording'}
        onPress={isRecording ? stopRecording : startRecording}
      />
    </View>
  );
};

export default App;
