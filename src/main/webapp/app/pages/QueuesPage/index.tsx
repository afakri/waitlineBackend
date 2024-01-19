import * as React from 'react';
import { CssVarsProvider } from '@mui/joy/styles';
import CssBaseline from '@mui/joy/CssBaseline';
import Box from '@mui/joy/Box';
import Breadcrumbs from '@mui/joy/Breadcrumbs';
import Link from '@mui/joy/Link';
import Typography from '@mui/joy/Typography';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import HomeRoundedIcon from '@mui/icons-material/HomeRounded';
import ChevronRightRoundedIcon from '@mui/icons-material/ChevronRightRounded';

import OrderTable from './components/OrderTable';
import OrderList from './components/OrderList';
import { Button } from '@mui/joy';
import Sidebar from 'app/shared/layout/SideBar/Sidebar';
import { useAppSelector } from 'app/config/store';
import { AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export default function QueuePage() {
  const account = useAppSelector(state => state.authentication.account);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const ribbonEnv = useAppSelector(state => state.applicationProfile.ribbonEnv);
  const isInProduction = useAppSelector(state => state.applicationProfile.inProduction);
  const isOpenAPIEnabled = useAppSelector(state => state.applicationProfile.isOpenAPIEnabled);

  return (
    <Box sx={{ display: 'flex', minHeight: '100dvh' }}>
      <Sidebar
        isAuthenticated={isAuthenticated}
        isAdmin={isAdmin}
        ribbonEnv={ribbonEnv}
        isInProduction={isInProduction}
        isOpenAPIEnabled={isOpenAPIEnabled}
      />
      <Box
        component="main"
        className="MainContent"
        sx={{
          px: { xs: 2, md: 6 },
          pt: {
            xs: 'calc(12px + var(--Header-height))',
            sm: 'calc(12px + var(--Header-height))',
            md: 3,
          },
          pb: { xs: 2, sm: 2, md: 3 },
          flex: 1,
          display: 'flex',
          flexDirection: 'column',
          minWidth: 0,
          height: '100dvh',
          gap: 1,
        }}
      >
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <Breadcrumbs size="sm" aria-label="breadcrumbs" separator={<ChevronRightRoundedIcon fontSize="small" />} sx={{ pl: 0 }}>
            <Link underline="none" color="neutral" href="#some-link" aria-label="Home">
              <HomeRoundedIcon />
            </Link>
            <Typography color="primary" fontWeight={500} fontSize={12}>
              Queues
            </Typography>
          </Breadcrumbs>
        </Box>
        <Box
          sx={{
            display: 'flex',
            mb: 1,
            gap: 1,
            flexDirection: { xs: 'column', sm: 'row' },
            alignItems: { xs: 'start', sm: 'center' },
            flexWrap: 'wrap',
            justifyContent: 'space-between',
          }}
        >
          <Typography level="h2" component="h1">
            Queues
          </Typography>
          <Button color="primary" startDecorator={<AddCircleOutlineIcon />} size="sm">
            Add Queue
          </Button>
        </Box>
        <OrderTable />
        <OrderList />
      </Box>
    </Box>
  );
}
