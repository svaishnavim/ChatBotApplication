package com.example.chatbotapplication

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel(){
    val messageList by lazy{
        mutableStateListOf<MessageModel>()
    }

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = Constants.apiKey
    )
    fun sendMessage(question:String){
       viewModelScope.launch {

           try {
               val chat = generativeModel.startChat(
                   //pass history of chat
                   history = messageList.map{
                       content(it.role){
                           text(it.message)
                       }
                   }.toList()
               )
               messageList.add(MessageModel(question, "user")) //send message
               messageList.add(MessageModel("Typing...", "model")) //when it is taking too long to load

               val response = chat.sendMessage(question)
               messageList.removeLast()
               messageList.add(MessageModel(response.text.toString(), "model")) //get response
           }
           catch (e:Exception){
               messageList.removeLast()
               messageList.add(MessageModel("error:"+e.message.toString(), "model"))
           }

       }
    }

}