/* eslint-disable jsx-a11y/anchor-is-valid */
import * as React from 'react';
import Button from '@mui/joy/Button';
import { Link } from 'react-router-dom';

import { getLoginUrl } from 'app/shared/util/url-utils';
import Typography from '@mui/joy/Typography';
import ArrowForward from '@mui/icons-material/ArrowForward';
import TwoSidedLayout from './TwoSidedLayout';

export default function WelcomePage() {
  return (
    <TwoSidedLayout>
      <Typography color="primary" fontSize="lg" fontWeight="lg">
        The power to do more
      </Typography>
      <Typography level="h1" fontWeight="xl" fontSize="clamp(1.875rem, 1.3636rem + 2.1818vw, 3rem)">
        A large headlinerer about our product features & services
      </Typography>
      <Typography fontSize="lg" textColor="text.secondary" lineHeight="lg">
        A descriptive secondary text placeholder. Use it to explain your business offer better.
      </Typography>
      <Button size="lg" onClick={() => {}} endDecorator={<ArrowForward fontSize="large" />}>
        Get Started
      </Button>
      <Typography>
        Already a member?{' '}
        <a href={getLoginUrl()}>
          <Button variant="outlined" onClick={() => {}}>
            {' '}
            Sign in{' '}
          </Button>
        </a>
      </Typography>
      <Typography
        level="body-xs"
        sx={{
          position: 'absolute',
          top: '2rem',
          left: '50%',
          transform: 'translateX(-50%)',
        }}
      >
        HeroLeft01
      </Typography>
    </TwoSidedLayout>
  );
}
