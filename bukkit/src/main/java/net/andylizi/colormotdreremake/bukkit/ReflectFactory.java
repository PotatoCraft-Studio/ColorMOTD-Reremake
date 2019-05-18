/*
 * Copyright (C) 2019 Bukkit Commons
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.andylizi.colormotdreremake.bukkit;


import com.comphenix.protocol.PacketType;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public final class ReflectFactory extends Object {
    public static Player[] getPlayers() {
        try {
            Method onlinePlayerMethod = Server.class.getMethod("getOnlinePlayers");
            if (onlinePlayerMethod.getReturnType().equals(Collection.class)) {
                return ((List<? extends Player>) onlinePlayerMethod.invoke(Bukkit.getServer())).toArray(new Player[0]);
            } else {
                return (Player[]) onlinePlayerMethod.invoke(Bukkit.getServer());
            }
        } catch (IndexOutOfBoundsException ex) {
            return new Player[0];
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
        return new Player[0];
    }
    
    public static PacketType getServerInfoPacketType() throws NoSuchFieldException{
        try{
            Field f = PacketType.Status.Server.class.getField("OUT_SERVER_INFO");
            f.setAccessible(true);
            return (PacketType) f.get(null);
        }catch(ReflectiveOperationException ex){
            try{
                Field f = PacketType.Status.Server.class.getField("SERVER_INFO");
                f.setAccessible(true);
                return (PacketType) f.get(null);
            }catch(ReflectiveOperationException ex2){
                throw new NoSuchFieldException("不支持的ProtocolLib版本");
            }
        }
    }
    
    public static PacketType getPingPacketType() throws NoSuchFieldException{
        try{
            Field f = PacketType.Status.Client.class.getField("IN_PING");
            f.setAccessible(true);
            return (PacketType) f.get(null);
        }catch(ReflectiveOperationException ex){
            try{
                Field f = PacketType.Status.Client.class.getField("PING");
                f.setAccessible(true);
                return (PacketType) f.get(null);
            }catch(ReflectiveOperationException ex2){
                throw new NoSuchFieldException("不支持的ProtocolLib版本");
            }
        }
    }
}
