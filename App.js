import React, {useState, useEffect} from 'react';
import {
  View,
  Text,
  NativeModules,
  ImageBackground,
  StyleSheet,
  TouchableOpacity,
  Linking,
  Platform,
  PermissionsAndroid,
  Alert,
} from 'react-native';
import moment from 'moment';

const {BoundServiceModule} = NativeModules;
const App = () => {
  const [isDayTime, setIsDayTime] = useState(true);
  const [currentDate, setCurrentDate] = useState('');
  const [isOnAccessibility, setAccessibility] = useState(true);
  const [havePermission, setHavePermisson] = useState(true);
  // Kiểm tra thời gian ban ngày hoặc ban đêm
  useEffect(() => {
    const currentTime = moment().format('HH:mm');
    const hour = parseInt(currentTime.split(':')[0]);
    // Xác định thời gian ban ngày hoặc ban đêm
    setIsDayTime(hour >= 6 && hour < 18);
    setCurrentDate(moment().format('DD MMMM, YYYY'));
  }, [havePermission]);
  const allow = () => {
    BoundServiceModule.allowPermission();
  };
  const setOnAccessibility = () => {
    if (!isOnAccessibility) {
      allow();
      setAccessibility(!isOnAccessibility);
    }
    setAccessibility(!isOnAccessibility);
  };
  const setPermission = async () => {
    Linking.openSettings();
  };
  const checkApplicationPermissions = async () => {
    if (Platform.OS === 'android') {
      try {
        await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.CAMERA);
        await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS,
        );
        await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
        );
      } catch (error) {
        console.log(error);
      }
    }
  };

  useEffect(() => {
    checkApplicationPermissions();
    if (PermissionsAndroid.RESULTS.DENIED) {
      setHavePermisson(false);
    }
    const requestPermission = async () => {
      try {
        const cameraPermission = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.CAMERA,
        );
        const notifyPermission = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS,
        );
        const audioPermission = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
        );
        if (
          cameraPermission != 'granted' ||
          notifyPermission != 'granted' ||
          audioPermission != 'granted'
        ) {
          setHavePermisson(false);
        } else {
          setHavePermisson(true);
        }
      } catch (error) {
        console.error('Error:', error);
      }
    };

    requestPermission();
  }, []);
  return (
    <ImageBackground
      source={isDayTime ? require('./day.jpg') : require('./night.jpg')}
      style={styles.container}>
      <View style={styles.item}>
        <Text
          style={[styles.textIntr, {color: isDayTime ? '#1D548A' : 'white'}]}>
          Welcome back, {isDayTime ? 'Good Morning' : 'Good Evening'}
        </Text>
        <Text
          style={[styles.textDate, {color: isDayTime ? '#1D548A' : 'white'}]}>
          {currentDate}
        </Text>
        <Text
          style={[
            styles.question,
            {color: isDayTime ? '#1D548A' : 'white', marginTop: 150},
          ]}>
          What do you need today?
        </Text>
        <View style={styles.settings}>
          <View style={styles.settingsContainer}>
            <TouchableOpacity
              style={[
                styles.outter,
                isOnAccessibility
                  ? {justifyContent: 'flex-end', backgroundColor: 'green'}
                  : {justifyContent: 'flex-start', backgroundColor: 'gray'},
              ]}
              activeOpacity={1}
              onPress={setOnAccessibility}>
              <View style={[styles.inner]} />
            </TouchableOpacity>
            <Text style={styles.settingsText}>
              {isOnAccessibility
                ? 'Permission granted, \nfeeling blessed! 🤤'
                : 'Accessibility permission needed, \nplease turn it on. 🥷'}
            </Text>
          </View>
          <View style={styles.settingsContainer}>
            <TouchableOpacity
              style={[
                styles.outter,
                havePermission
                  ? {justifyContent: 'flex-end', backgroundColor: 'green'}
                  : {justifyContent: 'flex-start', backgroundColor: 'gray'},
              ]}
              activeOpacity={1}
              onPress={setPermission}>
              <View style={[styles.inner]} />
            </TouchableOpacity>
            <Text style={styles.settingsText}>
              {havePermission
                ? 'Additional permissions\n are all set!! 😎'
                : 'Oh no, not just accessibility permission.\n We also need some more to help you! 😜'}
            </Text>
          </View>
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
  question: {
    fontSize: 25,
    fontFamily: 'Helvetica Neue',
    fontWeight: 'bold',
  },

  settings: {
    marginTop: '30%',
    paddingBottom: 20,
    paddingHorizontal: 10,
    borderRadius: 10,
    flexDirection: 'col',
    justifyContent: 'space-between',
    alignItems: 'left',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },

  settingsContainer: {
    marginTop: 20,
    flexDirection: 'row',
    alignItems: 'center',
  },

  settingsText: {
    fontSize: 14,
    marginLeft: 20,
    fontFamily: 'Helvetica Neue',
    fontWeight: 'bold',
    color: 'white',
  },

  inner: {
    width: 25,
    height: 25,
    backgroundColor: 'white',
    borderRadius: 15,
    elevation: 8,
    shadowOffset: {width: 0, height: 0},
    shadowOpacity: 0.15,
    shadowRadius: 2,
  },

  outter: {
    width: 50,
    height: 30,
    backgroundColor: 'gray',
    borderRadius: 15,
    paddingHorizontal: 2,
    alignItems: 'center',
    flexDirection: 'row',
  },
});
export default App;
