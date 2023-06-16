package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ChatMessageDto;
import com.example.BlaBlaBackend.Dto.ChatMessageResponse;
import com.example.BlaBlaBackend.Dto.ChatResponse;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.ChatMessage;
import com.example.BlaBlaBackend.entity.ChatRoom;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.UserGroupInfo;
import com.example.BlaBlaBackend.service.ChatMessageService;
import com.example.BlaBlaBackend.service.ChatRoomService;
import com.example.BlaBlaBackend.service.UserGroupInfoService;
import com.example.BlaBlaBackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

import static com.example.BlaBlaBackend.util.Helper.getRoomName;
import static com.example.BlaBlaBackend.util.Helper.getUserId;

@Controller
public class ChatController {
    private final SimpMessagingTemplate simpleMessagingTemplate;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ChatMessageService chatMessageService;
    private final UserGroupInfoService userGroupInfoService;

    public ChatController(SimpMessagingTemplate simpleMessagingTemplate, ChatRoomService chatRoomService, UserService userService, ChatMessageService chatMessageService, UserGroupInfoService userGroupInfoService) {
        this.simpleMessagingTemplate = simpleMessagingTemplate;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.chatMessageService = chatMessageService;
        this.userGroupInfoService = userGroupInfoService;
    }

    @MessageMapping("/chat")
    public void send(SimpMessageHeaderAccessor sha, @Payload ChatMessageDto chatMessageDto){
        String roomName = getRoomName(chatMessageDto.getSenderId(),chatMessageDto.getRecipientId());
        //String sender = sha.getUser().getName();
        int senderId = getUserId(sha.getUser().getName());
        ChatRoom chatRoom = chatRoomService.findChatRoomByChatRoomName(roomName);
        //if room doesn't exist!!!
        if(chatRoom==null) throw new ApiException(HttpStatus.valueOf(403),"Something went wrong");

        ChatMessage chatMessage = new ChatMessage()
                .sender(userService.getUserReferenceById(senderId))
                .recipient(userService.getUserReferenceById(chatMessageDto.getRecipientId()))
                .message(chatMessageDto.getContent())
                .chatRoom(chatRoom);

        //saving the message in database!!
        chatMessageService.saveChatMessageInDb(chatMessage);
        //creating chat response!!
        ChatMessageResponse chatMessageResponse = new ChatMessageResponse()
                .message(chatMessage.getMessage())
                .roomName(chatRoom.getChatRoomName())
                .publishTime(chatMessage.getMessageSendAt());
        //sending the message to the end user!!!
        simpleMessagingTemplate.convertAndSendToUser("user_"+chatMessageDto.getRecipientId(), "/queue/messages", chatMessageResponse);
    }
//    @GetMapping("/api/get-room/chats")
//    @ResponseBody
//    public List<ChatResponse> getChatRoomMessages(@RequestParam String roomName){
//        ChatRoom chatRoom = chatRoomService.findChatRoomByChatRoomName(roomName);
//        System.out.println(chatRoom.getRoomId()+"///////////");
//        return chatMessageService.getChatMessageByRoomName(chatRoom);
//    }
    @PostMapping("/api/createRoom")
    @ResponseBody
    public String createRoomUtil(@RequestBody HashMap<String,Integer> mp){
        User user1 = userService.getUserReferenceById(mp.get("f"));
        User user2 = userService.getUserReferenceById(mp.get("l"));
        String roomName = getRoomName(user1.grabCurrentUserId(),user2.grabCurrentUserId());
        ChatRoom chatRoom = chatRoomService.findChatRoomByChatRoomName(roomName);
        //if room doesn't exist!!!
        if(chatRoom==null){
            chatRoom = chatRoomService.createChatRoom(new ChatRoom().chatRoomName(roomName));
            userGroupInfoService.saveUserGroupInfo(new UserGroupInfo().chatRoom(chatRoom).user(user1));
            userGroupInfoService.saveUserGroupInfo(new UserGroupInfo().chatRoom(chatRoom).user(user2));
        }
        return "Room Created Successfully!!";
    }
    @PostMapping("/send/chat")
    @ResponseBody
    public ChatMessageResponse sendMessage(@RequestHeader String Authorization, @RequestBody ChatMessageDto chatMessageDto){
        String roomName = getRoomName(chatMessageDto.getSenderId(),chatMessageDto.getRecipientId());
        //String sender = sha.getUser().getName();
       /// int senderId = Integer.parseInt(jwtProvider.getUsernameFromToken(Authorization.substring(7)));
        ChatRoom chatRoom = chatRoomService.findChatRoomByChatRoomName(roomName);
        //if room doesn't exist!!!
        if(chatRoom==null) throw new ApiException(HttpStatus.valueOf(403),"Something went wrong");

        ChatMessage chatMessage = new ChatMessage()
                .sender(userService.getUserReferenceById(chatMessageDto.getSenderId()))
                .recipient(userService.getUserReferenceById(chatMessageDto.getRecipientId()))
                .message(chatMessageDto.getContent())
                .chatRoom(chatRoom);

        //saving the message in database!!
        chatMessageService.saveChatMessageInDb(chatMessage);
       // return "Chat message send Successfully!!!";
        //creating chat response!!
        ChatMessageResponse chatMessageResponse = new ChatMessageResponse()
                .message(chatMessage.getMessage())
                .roomName(chatRoom.getChatRoomName())
                .publishTime(chatMessage.getMessageSendAt());
        return chatMessageResponse;
//        //sending the message to the end user!!!
//        simpleMessagingTemplate.convertAndSendToUser("user_"+chatMessageDto.getRecipientId(), "/queue/messages", chatMessageResponse);
    }
    @GetMapping("/api/get-room/chats") @ResponseBody
    public List<ChatResponse> getRoomChats(@RequestParam String roomName){
        ChatRoom chatRoom = chatRoomService.findChatRoomByChatRoomName(roomName);
        List<ChatResponse> chatResponses =  chatMessageService.findChatMessages(chatRoom.getRoomId());
        return chatResponses;
    }
    //step1: store message --roomId,senderId,recieverId,roomId
    //step2: send message!!!
    //@MessageMapping("/chat")
//    public void send(SimpMessageHeaderAccessor sha, @Payload ChatMessage chatMessage) throws Exception {
//        String sender = sha.getUser().getName();//sender session id store kr lunga
//
//
////        ChatMessage message = new ChatMessage(chatMessage.getFrom(), chatMessage.getText(), chatMessage.getRecipient());
////        if (!sender.equals(chatMessage.getRecipient())) {
////            simpMessagingTemplate.convertAndSendToUser(sender, "/queue/messages", message);
////        }
//        simpMessagingTemplate.convertAndSendToUser(chatMessage.getRecipient(), "/queue/messages", message);
//    }
}
