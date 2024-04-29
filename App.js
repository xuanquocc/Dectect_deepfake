import React, { useEffect } from 'react';
import { View, Text } from 'react-native';

const App = () => {
  useEffect(() => {
    const uploadVideo = async () => {
      try {
        const videoData = new FormData();
        videoData.append('video_file', {
          uri: 'file:///C:/Users/Admin/Desktop/Deepfake/src/assets/video/real_1.mp4',
          type: 'video/mp4',
          name: 'real_1.mp4',
        });

        const response = await fetch("https://api-deepfake-detection-1.onrender.com/upload-video/", {
          method: 'POST',
          body: videoData,
          headers: {
            Accept: 'application/json',
            'Content-Type': 'multipart/form-data',
          },
        });

        const data = await response.json();
        console.log(data);
      } catch (error) {
        console.error(error);
      }
    };

    uploadVideo();
  }, []);

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text>hello</Text>
    </View>
  );
};

export default App;
