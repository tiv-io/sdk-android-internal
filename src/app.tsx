/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import type { Node } from 'react';
import {
    NativeEventEmitter,
    NativeModules,
    Text,
} from 'react-native';

import * as bridge from './bridge'
console.log('bridge: ', bridge);

const App: () => Node = () => {
    return <Text>Ahoj!</Text>
};

export default App;
