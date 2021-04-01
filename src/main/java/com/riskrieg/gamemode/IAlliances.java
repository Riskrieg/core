package com.riskrieg.gamemode;

import com.riskrieg.nation.AllianceNation;
import com.riskrieg.response.Response;

public interface IAlliances {

  boolean allied(AllianceNation a, AllianceNation b);

  boolean allied(String idA, String idB);

  Response ally(AllianceNation a, AllianceNation b);

  Response ally(String idA, String idB);

  Response unally(AllianceNation a, AllianceNation b);

  Response unally(String idA, String idB);

  Response dissolveAlliances(AllianceNation nation);

  Response dissolveAlliances(String id);

}
