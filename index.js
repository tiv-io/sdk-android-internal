/**
 * @format
 */

import { decode, encode } from 'base-64'
import { AppRegistry, LogBox } from 'react-native';
import App from './src/app';
import { name as appName } from './app.json';

if (!global.btoa) { global.btoa = encode }
if (!global.atob) { global.atob = decode }

AppRegistry.registerComponent(appName, () => App);
