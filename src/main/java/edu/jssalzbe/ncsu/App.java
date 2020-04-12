package edu.jssalzbe.ncsu;

import java.io.File;
import java.util.Scanner;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] arg) throws Exception {
        Scanner s = new Scanner(new File("password.txt"));
        String pw = s.nextLine();
        s.close();
        DiscordClientBuilder b = DiscordClientBuilder.create(pw);
        DiscordClient client = b.build();

        client.getEventDispatcher().on(ReadyEvent.class).subscribe(event -> {
            User self = event.getSelf();
            System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
        });

        ReactionEmoji e = ReactionEmoji.unicode("❤️");

        client.getEventDispatcher().on(MessageCreateEvent.class).map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(message -> message.addReaction(e)).subscribe();
        client.login().block();
    }
}
