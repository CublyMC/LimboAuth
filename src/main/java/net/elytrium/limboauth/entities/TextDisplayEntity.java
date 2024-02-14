/*
 * Copyright (C) 2021 - 2024 Elytrium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.elytrium.limboauth.entities;

import com.velocitypowered.api.network.ProtocolVersion;
import java.util.Map;
import java.util.UUID;
import net.elytrium.limboapi.api.player.LimboPlayer;
import net.elytrium.limboauth.protocol.metadata.EntityMetadata;
import net.elytrium.limboauth.protocol.metadata.EntityMetadataByteEntry;
import net.elytrium.limboauth.protocol.metadata.EntityMetadataComponentEntry;
import net.elytrium.limboauth.protocol.metadata.EntityMetadataVarIntEntry;
import net.elytrium.limboauth.protocol.packets.SetEntityMetadata;
import net.elytrium.limboauth.protocol.packets.SpawnEntity;
import net.kyori.adventure.text.Component;

public class TextDisplayEntity {

  private final int entityId;

  private final double positionX;
  private final double positionY;
  private final double positionZ;
  private final byte billboardConstraints;
  private final int backgroundColor;
  private final Component text;


  public TextDisplayEntity(int entityId, double positionX, double positionY, double positionZ,
      byte billboardConstraints, int backgroundColor, Component text) {
    this.entityId = entityId;
    this.positionX = positionX;
    this.positionY = positionY;
    this.positionZ = positionZ;
    this.billboardConstraints = billboardConstraints;
    this.backgroundColor = backgroundColor;
    this.text = text;
  }

  public void spawn(LimboPlayer player) {
    player.writePacketAndFlush(new SpawnEntity(this.entityId, UUID.randomUUID(), version -> 101, this.positionX,
        this.positionY, this.positionZ, 0, 0, 0, 0));
    player.writePacketAndFlush(new SetEntityMetadata(this.entityId, this::buildMetadata));
  }

  private EntityMetadata buildMetadata(ProtocolVersion version) {
    return new EntityMetadata(
        Map.of(
            (byte) 15, new EntityMetadataByteEntry(this.billboardConstraints),
            (byte) 23, new EntityMetadataComponentEntry(this.text),
            (byte) 25, new EntityMetadataVarIntEntry(this.backgroundColor)
        )
    );
  }

}
