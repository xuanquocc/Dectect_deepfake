import React, { useState, useEffect } from 'react';
import { View, Text, Button } from 'react-native';
import CountingService from './CountingServiceModule';

const App = () => {
  const [count, setCount] = useState(0);
  const [isCounting, setIsCounting] = useState(false);

  useEffect(() => {
    const interval = setInterval(() => {
      if (isCounting) {
        setCount((prevCount) => prevCount + 1);
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

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text style={{ fontSize: 24, marginBottom: 20 }}>Count: {count}</Text>
      <Button title="Start" onPress={startCounting} disabled={isCounting} />
      <Button title="Stop" onPress={stopCounting} disabled={!isCounting} />
    </View>
  );
};

export default App;
