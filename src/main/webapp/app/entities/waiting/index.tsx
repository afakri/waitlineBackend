import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Waiting from './waiting';
import WaitingDetail from './waiting-detail';
import WaitingUpdate from './waiting-update';
import WaitingDeleteDialog from './waiting-delete-dialog';

const WaitingRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Waiting />} />
    <Route path="new" element={<WaitingUpdate />} />
    <Route path=":id">
      <Route index element={<WaitingDetail />} />
      <Route path="edit" element={<WaitingUpdate />} />
      <Route path="delete" element={<WaitingDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WaitingRoutes;
