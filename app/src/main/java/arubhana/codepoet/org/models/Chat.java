package arubhana.codepoet.org.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

public class Chat implements RoomListener{
Scaledrone drone;

public Chat(final String room){
    drone=new Scaledrone("TZh2rW1rt9lQbRnc");
    drone.connect(new Listener() {
        @Override
        public void onOpen() {
            drone.subscribe(room,Chat.this);
        }

        @Override
        public void onOpenFailure(Exception ex) {

        }

        @Override
        public void onFailure(Exception ex) {

        }

        @Override
        public void onClosed(String reason) {

        }
    });

}

    @Override
    public void onOpen(Room room) {

    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {

    }

    @Override
    public void onMessage(Room room, JsonNode message, Member member) {

    }
}
