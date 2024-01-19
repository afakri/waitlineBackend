import * as React from 'react';
import './ribbon.scss';
import GlobalStyles from '@mui/joy/GlobalStyles';
import Avatar from '@mui/joy/Avatar';
import Box from '@mui/joy/Box';
import Divider from '@mui/joy/Divider';
import IconButton from '@mui/joy/IconButton';
import Input from '@mui/joy/Input';
import List from '@mui/joy/List';
import ListItem from '@mui/joy/ListItem';
import ListItemButton, { listItemButtonClasses } from '@mui/joy/ListItemButton';
import ListItemContent from '@mui/joy/ListItemContent';
import Typography from '@mui/joy/Typography';
import Sheet from '@mui/joy/Sheet';
import SearchRoundedIcon from '@mui/icons-material/SearchRounded';
import HomeRoundedIcon from '@mui/icons-material/HomeRounded';
import DashboardRoundedIcon from '@mui/icons-material/DashboardRounded';
import FormatListNumberedRtlIcon from '@mui/icons-material/FormatListNumberedRtl';
import SupportRoundedIcon from '@mui/icons-material/SupportRounded';
import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import LogoutRoundedIcon from '@mui/icons-material/LogoutRounded';
import BrightnessAutoRoundedIcon from '@mui/icons-material/BrightnessAutoRounded';
import { Link } from 'react-router-dom';
import ColorSchemeToggle from './ColorSchemeToggle';
import { closeSidebar } from './utils';
import MenuBookIcon from '@mui/icons-material/MenuBook';
export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
}
const openAPIItem = () => (
  <Link to="/admin/docs" style={{ textDecoration: 'none' }}>
    <ListItem>
      <ListItemButton>
        <MenuBookIcon />
        <ListItemContent>
          <Typography level="title-sm">API</Typography>
        </ListItemContent>
      </ListItemButton>
    </ListItem>
  </Link>
);

export default function Sidebar(props: IHeaderProps) {
  const renderDevRibbon = () =>
    props.isInProduction ? (
      <div className="ribbon dev">
        <a href="">Development</a>
      </div>
    ) : null;

  return (
    <React.Fragment>
      {renderDevRibbon()}
      <Sheet
        className="Sidebar"
        sx={{
          position: { xs: 'fixed', md: 'sticky' },
          transform: {
            xs: 'translateX(calc(100% * (var(--SideNavigation-slideIn, 0) - 1)))',
            md: 'none',
          },
          transition: 'transform 0.4s, width 0.4s',
          zIndex: 10000,
          height: '100dvh',
          width: 'var(--Sidebar-width)',
          top: 0,
          p: 2,
          flexShrink: 0,
          display: 'flex',
          flexDirection: 'column',
          gap: 2,
          borderRight: '1px solid',
          borderColor: 'divider',
        }}
      >
        <GlobalStyles
          styles={theme => ({
            ':root': {
              '--Sidebar-width': '220px',
              [theme.breakpoints.up('lg')]: {
                '--Sidebar-width': '280px',
              },
            },
          })}
        />
        <Box
          className="Sidebar-overlay"
          sx={{
            position: 'fixed',
            zIndex: 9998,
            top: 0,
            left: 0,
            width: '100vw',
            height: '100vh',
            opacity: 'var(--SideNavigation-slideIn)',
            backgroundColor: 'var(--joy-palette-background-backdrop)',
            transition: 'opacity 0.4s',
            transform: {
              xs: 'translateX(calc(100% * (var(--SideNavigation-slideIn, 0) - 1) + var(--SideNavigation-slideIn, 0) * var(--Sidebar-width, 0px)))',
              lg: 'translateX(-100%)',
            },
          }}
          onClick={() => closeSidebar()}
        />
        <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
          <IconButton variant="soft" color="primary" size="sm">
            <BrightnessAutoRoundedIcon />
          </IconButton>
          <Typography level="title-lg">Acme Co.</Typography>
          <ColorSchemeToggle sx={{ ml: 'auto' }} />
        </Box>
        <Input size="sm" startDecorator={<SearchRoundedIcon />} placeholder="Search" />
        <Box
          sx={{
            minHeight: 0,
            overflow: 'hidden auto',
            flexGrow: 1,
            display: 'flex',
            flexDirection: 'column',
            [`& .${listItemButtonClasses.root}`]: {
              gap: 1.5,
            },
          }}
        >
          <List
            size="sm"
            sx={{
              gap: 1,
              '--List-nestedInsetStart': '30px',
              '--ListItem-radius': theme => theme.vars.radius.sm,
            }}
          >
            <Link to="/" style={{ textDecoration: 'none' }}>
              <ListItem>
                <ListItemButton>
                  <HomeRoundedIcon />
                  <ListItemContent>
                    <Typography level="title-sm">Home</Typography>
                  </ListItemContent>
                </ListItemButton>
              </ListItem>
            </Link>

            <ListItem>
              <ListItemButton>
                <DashboardRoundedIcon />
                <ListItemContent>
                  <Typography level="title-sm">Dashboard</Typography>
                </ListItemContent>
              </ListItemButton>
            </ListItem>
            <Link to="/queues" style={{ textDecoration: 'none' }}>
              <ListItem>
                <ListItemButton>
                  <FormatListNumberedRtlIcon />
                  <ListItemContent>
                    <Typography level="title-sm">Queues</Typography>
                  </ListItemContent>
                </ListItemButton>
              </ListItem>
            </Link>
            <ListItem>
              <ListItemButton>
                <SupportRoundedIcon />
                Support
              </ListItemButton>
            </ListItem>
            <ListItem>
              <ListItemButton>
                <SettingsRoundedIcon />
                Settings
              </ListItemButton>
            </ListItem>
            {props.isAuthenticated && props.isAdmin && openAPIItem()}
          </List>
        </Box>
        <Divider />
        <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
          <Avatar variant="outlined" size="sm" src={''} />
          <Box sx={{ minWidth: 0, flex: 1 }}>
            <Typography level="title-sm">Ayman</Typography>
            <Typography level="body-xs">aymanfakri@gmail.com</Typography>
          </Box>
          <Link to="/logout">
            <IconButton size="sm" variant="plain" color="neutral" onClick={() => {}}>
              <LogoutRoundedIcon />
            </IconButton>
          </Link>
        </Box>
      </Sheet>
    </React.Fragment>
  );
}
