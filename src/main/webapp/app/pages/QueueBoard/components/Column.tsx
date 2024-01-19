import { Avatar, Box, Card, List, ListDivider, ListItem, ListItemButton, Button, Typography } from '@mui/joy';
import * as React from 'react';
import { Droppable, Draggable } from 'react-beautiful-dnd';
import DragIndicatorIcon from '@mui/icons-material/DragIndicator';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
export default function Column(props) {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        rowGap: 1,
        width: 300,
        height: '100%',
      }}
    >
      <Card
        color={props.color}
        sx={{
          display: 'flex',
          flexDirection: 'row',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        <Typography level="title-lg">{props.title}</Typography>
        <Button variant="plain" color={props.color}>
          <AddCircleOutlineIcon />
        </Button>
      </Card>
      <Droppable droppableId={props.data.name} type="group">
        {provided => (
          <List
            variant="outlined"
            {...provided.droppableProps}
            ref={provided.innerRef}
            sx={{
              width: '100%',
            }}
          >
            {props.data.items.map((item, index) => (
              <Draggable index={index} draggableId={item.id} key={item.id}>
                {provided => (
                  <div {...provided.dragHandleProps} {...provided.draggableProps} ref={provided.innerRef}>
                    <ListItem>
                      <ListItemButton sx={{ p: 2 }}>
                        <DragIndicatorIcon />
                        <Box
                          sx={{
                            pl: 2,
                            display: 'flex',
                            flexDirection: 'column',
                          }}
                        >
                          <Box
                            sx={{
                              mb: 0.5,
                            }}
                          >
                            <Typography level="body-xs" textColor="text.tertiary">
                              {new Date().toLocaleTimeString()}
                            </Typography>
                          </Box>
                          <div>
                            <Typography level="title-sm" sx={{ mb: 0.5 }}>
                              {item.name}
                            </Typography>
                          </div>
                        </Box>
                      </ListItemButton>
                    </ListItem>
                    <ListDivider sx={{ m: 0 }} />
                  </div>
                )}
              </Draggable>
            ))}
            {provided.placeholder}
          </List>
        )}
      </Droppable>
    </Box>
  );
}
