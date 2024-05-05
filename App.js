import React, {useState, useEffect} from 'react';
import {
  View,
  Text,
  Button,
  NativeModules,
  ImageBackground,
  StyleSheet,
  TouchableOpacity,
  Linking 
} from 'react-native';
import moment from 'moment';

const App = () => {
  const [isDayTime, setIsDayTime] = useState(true);
  const [currentDate, setCurrentDate] = useState('');
  // Kiểm tra thời gian ban ngày hoặc ban đêm
  useEffect(() => {
    const currentTime = moment().format('HH:mm');
    const hour = parseInt(currentTime.split(':')[0]);
    // Xác định thời gian ban ngày hoặc ban đêm
    setIsDayTime(hour >= 6 && hour < 18);
    setCurrentDate(moment().format('DD MMMM, YYYY'));
  }, []);
  const openEvent = () => 
    {
      const youtubeURL = 'https://www.youtube.com/watch?v=dQw4w9WgXcQ'; // Đường dẫn của trang web YouTube bạn muốn mở
      Linking.openURL(youtubeURL);
    }
  return (
    <ImageBackground
      source={isDayTime ? require('./day.jpg') : require('./night.jpg')}
      style={styles.container}>
      <View style={styles.item}>
        <Text
          style={[styles.textIntr, {color: isDayTime ? '#1D548A' : 'white'}]}>
          WELCOME BACK, MY NIGGAR
        </Text>
        <Text
          style={[styles.textDate, {color: isDayTime ? '#1D548A' : 'white'}]}>
           {currentDate}
        </Text>

        <View style={styles.box}>
          <Text style={styles.boxText}>The number has been detected.</Text>
          <Text style={styles.boxNumber}>1000</Text>
        </View>
        <Text
          style={[
            styles.question,
            {color: isDayTime ? '#1D548A' : 'white', marginTop: 200},
          ]}>
          Why are you gay?
        </Text>
        <View style={styles.settingsContainer}>
          <TouchableOpacity style={styles.settingsButton} onPress={openEvent}>
            <Text style={styles.icon}>⚙️</Text>
          </TouchableOpacity>
          <Text style={styles.settingsText}>Gỡ cài đặt</Text>
        </View>
      </View>
    </ImageBackground>
  );
};
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  item: {
    flex: 1,
    padding: 50,
  },
  textIntr: {
    fontSize: 30,
    fontFamily: 'Helvetica Neue',
    fontWeight: 'bold',
  },
  textDate: {
    fontSize: 15,
    fontFamily: 'Helvetica Neue',
    fontWeight: 'bold',
  },
  box: {
    width: '100%',
    height: 100,
    backgroundColor: 'white',
    alignItems: 'center',
    marginTop: 200,
    borderRadius: 10,
    shadowColor: 'rgba(0, 0, 255, 1.6)',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.5,
    shadowRadius: 10,
    elevation: 10,
  },
  boxText: {
    paddingTop: 20,
    color: 'black',
    fontSize: 18,
  },
  boxNumber: {
    paddingTop: 20,
    color: 'green', 
  },
  question: {
    fontSize: 22,
    fontFamily: 'Helvetica Neue',
    fontWeight: 'bold',
  },
  settingsContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 20,
  },
  settingsButton: {
    padding: 10,
    borderRadius: 30,
    backgroundColor: '#f0f0f0',
    alignItems: 'center',
    width: 60,
  },
  icon: {
    fontSize: 30,
  },
  settingsText: {
    fontSize: 18,
    marginLeft: 20,
    fontFamily: 'Helvetica Neue',
    fontWeight: 'bold',
    color: 'white',
  },
});
export default App;
