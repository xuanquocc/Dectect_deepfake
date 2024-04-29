import React, { useState, useEffect } from 'react';
import { View, Text, Button } from 'react-native';
import CountingService from './src/modules/CountingServiceModule';

const App = () => {

  const startCounting = () => {
    CountingService.startCounting();
  };

  const stopCounting = () => {
    CountingService.stopCounting();
  };
  const allow = () => {
    CountingService.allowPermission();
  }
  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Button title="Start music" onPress={startCounting}  />
      <Button title="Stop music" onPress={stopCounting} />
      <Button title="Allow permission" onPress={allow}/>
    </View>
  );
};

export default App;
