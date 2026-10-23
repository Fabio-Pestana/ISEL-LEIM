using System.Collections;
using System.Collections.Generic;
using System.Security.Cryptography;
using UnityEngine;

/// <summary>
/// Classe auxiliar para geração de altura e ruído.
/// </summary>
public class Utils
{
    static float smooth = 0.0015f;          // menor -> mais plano o terreno. 0.0002f mais plano
    static float smooth3D = 10f * smooth;   // Fator de suavização para ruído 3D

    static int maxHeight = 150;             // Altura máxima do terreno
    static int octaves = 6;                 // Número de octaves para o ruído
    static float persistence = 0.7f;        // Persistência do ruído
    static float offset = 32000f;           // Offset para o ruído


    /// <summary>
    /// Gera a altura do terreno em um ponto específico.
    /// </summary>
    /// <param name="x">Coordenada x.</param>
    /// <param name="z">Coordenada z.</param>
    /// <returns>Altura do terreno.</returns>
    public static int GenerateHeight(float x, float z)
    {
        return (int) Map(0, maxHeight, 0, 1, fBM(x * smooth, z * smooth, octaves, persistence));
    }

    /// <summary>
    /// Gera a altura da camada de pedra em um ponto específico.
    /// </summary>
    /// <param name="x">Coordenada x.</param>
    /// <param name="z">Coordenada z.</param>
    /// <returns>Altura da camada de pedra.</returns>
    public static int GenerateStoneHeight(float x, float z)
    {
        return (int)Map(0, maxHeight - 10, 0, 1, fBM(x * 3 * smooth, z * 3 * smooth, octaves -1 , 1.2f * persistence));
    }

    /// <summary>
    /// Mapeia um valor de um intervalo para outro.
    /// </summary>
    /// <param name="newmin">Novo valor mínimo.</param>
    /// <param name="newmax">Novo valor máximo.</param>
    /// <param name="orimin">Valor mínimo original.</param>
    /// <param name="orimax">Valor máximo original.</param>
    /// <param name="val">Valor a ser mapeado.</param>
    /// <returns>Valor mapeado.</returns>
    static float Map(float newmin, float newmax, float orimin, float orimax, float val)
    {
        return Mathf.Lerp(newmin, newmax, Mathf.InverseLerp(orimin, orimax, val));
    }

    /// <summary>
    /// Gera ruído 3D utilizando o método fBM.
    /// </summary>
    /// <param name="x">Coordenada x.</param>
    /// <param name="y">Coordenada y.</param>
    /// <param name="z">Coordenada z.</param>
    /// <param name="octaves">Número de octaves.</param>
    /// <param name="persistence">Persistência do ruído.</param>
    /// <returns>Valor do ruído 3D.</returns>
    public static float fBM3D (float x,float y, float z, int octaves, float persistence)
    {
        float xy = fBM(x * smooth3D, y * smooth3D, octaves, persistence);
        float yx = fBM(y * smooth3D, x * smooth3D, octaves, persistence);

        float xz = fBM(x * smooth3D, z * smooth3D, octaves, persistence);
        float zx = fBM(z * smooth3D, x * smooth3D, octaves, persistence);

        float yz = fBM(y * smooth3D, z * smooth3D, octaves, persistence);
        float zy = fBM(z * smooth3D, y * smooth3D, octaves, persistence);

        return (xy + yx + xz + zx + yz + zy) / 6;
    }

    /// <summary>
    /// Gera ruído utilizando o método fBM.
    /// </summary>
    /// <param name="x">Coordenada x.</param>
    /// <param name="z">Coordenada z.</param>
    /// <param name="octaves">Número de octaves.</param>
    /// <param name="persistence">Persistência do ruído.</param>
    /// <returns>Valor do ruído.</returns>
    static float fBM(float x, float z, int octaves, float persistence)
    {
        float total = 0;
        float amplitude = 1;
        float frequency = 1;
        float maxValue = 0;

        for (int i = 0; i < octaves; i++)
        {
            total += Mathf.PerlinNoise((x + offset) * frequency, (z + offset) * frequency) * amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= 2;
        }
        return total/maxValue;
    }
}
