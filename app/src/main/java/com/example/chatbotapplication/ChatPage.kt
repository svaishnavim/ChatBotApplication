package com.example.chatbotapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.*
import com.example.chatbotapplication.ui.theme.*


@Composable
fun ChatPage(modifier:Modifier = Modifier, viewModel: ChatViewModel){
    Column(modifier=modifier){
        AppHeader()
        MessageList(modifier = Modifier.weight(1f), messageList = viewModel.messageList)
        MessageInput(onMessageSend = {
            viewModel.sendMessage(it)
        })
    }

}

@Composable
fun MessageList(modifier: Modifier, messageList:List<MessageModel>){
    LazyColumn(modifier=modifier,
        reverseLayout = true){
        items(messageList.reversed()){
            MessageRow(messageModel = it)
        }
    }

}
@Composable
fun MessageRow(messageModel: MessageModel){
    val isModel = messageModel.role=="model"
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(modifier = Modifier.fillMaxWidth()){
            Box(
                modifier = Modifier
                    .align(
                        if (isModel) Alignment.BottomStart else Alignment.BottomEnd
                    )
                    .padding(
                        start = if (isModel) 8.dp else 70.dp, end = if (isModel) 70.dp else 8.dp,
                        top = 10.dp, bottom = 10.dp
                    )
                    .clip(RoundedCornerShape(48f))
                    .background(if (isModel) ColorModelMessage else ColorUserMessage)
                    .padding(16.dp)
            ){
                //to copy message
                SelectionContainer {
                    Text(
                        text = messageModel.message,
                        fontWeight = FontWeight.W500,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun MessageInput(onMessageSend : (String)-> Unit){
    var message by remember{
        mutableStateOf("")
    }
    Row(modifier=Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = message, onValueChange = {
                message=it
            })

            IconButton(onClick = {
                if(message.isNotEmpty()){ //cannot send empty msg
                    onMessageSend(message)
                    message=""
                }

            }){
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
    }
}


@Composable
fun AppHeader(){
    Box(modifier= Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.tertiary)){
        Text(modifier = Modifier.padding(16.dp), text="Chat Bot",
            color= Color.White,
            fontSize=22.sp
        )
    }
}


