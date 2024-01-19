import React from 'react';
import { Route } from 'react-router-dom';
import Loadable from 'react-loadable';

import LoginRedirect from 'app/modules/login/login-redirect';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PageNotFound from 'app/shared/error/page-not-found';
import { AUTHORITIES } from 'app/config/constants';
import QueueBoard from './pages/QueueBoard';
import QueuePage from './pages/QueuesPage';
import Queue from './entities/queue/queue';
import QueueRoutes from './entities/queue';

const loading = <div>loading ...</div>;

const Admin = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => loading,
});
const AppRoutes = () => {
  return (
    <div className="view-routes">
      <ErrorBoundaryRoutes>
        <Route index element={<Home />} />
        <Route path="logout" element={<Logout />} />

        <Route path="board/*" element={<QueueBoard />} />
        <Route path="queues/*" element={<QueuePage />} />
        <Route
          path="admin/*"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
              <Admin />
            </PrivateRoute>
          }
        />
        <Route path="oauth2/authorization/oidc" element={<LoginRedirect />} />
        <Route path="*" element={<PageNotFound />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};

export default AppRoutes;
