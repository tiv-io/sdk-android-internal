import { Conf, createTivio } from '@tivio/sdk-js'
import type { Api, TivioPlayerWrapper as PlayerWrapper, Source, AdMetadata } from '@tivio/sdk-js'

import {
    NativeEventEmitter,
    NativeModules,
} from 'react-native';

console.log('Initializing native bridge');

const { TivioManager, TivioPlayerWrapper } = NativeModules

const tivioEmmiter = new NativeEventEmitter(TivioManager)
const playerWrapperEmmiter = new NativeEventEmitter(TivioPlayerWrapper);

const initTivio = createTivio()
const playerWrappers: { [id: number]: any } = {}

let tivioApi: Api | null = null

export const getPlayerWrapper = (playerWrapperId: number): any | null => {
    console.log('getPlayerWrapper with id:', playerWrapperId, playerWrappers[playerWrapperId], tivioApi !== null);
    if (!playerWrappers[playerWrapperId] && tivioApi) {
        console.log('Creating new PlayerWrapper(js)');
        playerWrappers[playerWrapperId] = tivioApi.createPlayerWrapper?.({
            setSource: (source: any | null) => {
                console.log('Received source from Tivio', source?.uri)
                TivioPlayerWrapper.setSource({
                    ...source,
                    playerWrapperId
                })
            },
            seekTo: (miliseconds: number) => {
                console.log(`Received seek from Tivio: ${miliseconds} ms`)
                TivioPlayerWrapper.seekTo({
                    miliseconds,
                    playerWrapperId,
                })
            },
        })
    }

    console.log('getPlayerWrapper: ', playerWrappers[playerWrapperId]);
    return playerWrappers[playerWrapperId] || null
}

tivioEmmiter.addListener('init', async (event) => {
    console.log('Tivio init', event);
    if (tivioApi) throw new Error('Tivio can not be initialized more than once')

    tivioApi = await initTivio({
        currency: 'EUR',
        deviceCapabilities: event.deviceCapabilities,
        enableSentry: false,
        secret: event.secret,
        verbose: true,
    } as any as Conf)

    console.log('Tivio is initialized');
})

playerWrapperEmmiter.addListener('reportTimeProgress', (event) => {
    const playerWrapper = getPlayerWrapper(event.playerWrapperId)

    playerWrapper?.reportTimeProgress(event.miliseconds)
})

playerWrapperEmmiter.addListener('seekTo', (event) => {
    const playerWrapper = getPlayerWrapper(event.playerWrapperId)

    playerWrapper?.seekTo(event.miliseconds)
})

playerWrapperEmmiter.addListener('setSource', (event) => {
    console.log('TivioPlayerWrapper setSource event:', event);

    const playerWrapper = getPlayerWrapper(event.playerWrapperId)
    console.log('playerWrapper setSource: ', playerWrapper, event);

    playerWrapper?.setSource({
        ...event,
        type: 'tv_program',
        startFromPosition: event.startFromPosition,
        streamStart: new Date(event.streamStart),
        epgFrom: new Date(event.epgFrom),
        epgTo: new Date(event.epgTo),
    })
})

TivioManager.initTivio()
