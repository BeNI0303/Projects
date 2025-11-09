import { configureStore } from '@reduxjs/toolkit';
import reservationProgressReducer from './reservationProgressSlice';

export const store = configureStore({
  reducer: {
    reservationProgress: reservationProgressReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
});