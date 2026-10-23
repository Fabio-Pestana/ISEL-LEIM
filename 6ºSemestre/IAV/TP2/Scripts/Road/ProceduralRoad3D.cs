using PathCreation;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ProceduralRoad3D : MonoBehaviour
{
    public int numSegments;
    public float distanceBetweenAnchors;

    public float smooth;

    public Transform car;
    RoadMesh road;

    float angleOffset;


    void Start()
    {
        angleOffset = car.localEulerAngles.y * Mathf.Deg2Rad - PerlinOrientation(car.position);
        Vector3 pos = car.position;
        pos.y = Terrain.activeTerrain.SampleHeight(pos) + 3;
        car.position = pos;

        road = GetComponent<RoadMesh>();
        BuildRoadAhead(car.position - car.forward * 4);
    }

    void BuildRoadAhead(Vector3 startPosition)
    {
        List<Vector3> anchors = new List<Vector3>();
        anchors.Add(startPosition);

        for (int i = 0; i < numSegments; i++)
        {
            Vector3 newAnchor = NextAnchor(anchors[i]);
            anchors.Add(newAnchor);
        }

        VertexPath vertexPath = GeneratePath(anchors, 0.2f, false, PathSpace.xyz);
        road.BuildRoad(vertexPath);
    }

    Vector3 NextAnchor(Vector3 anchor)
    {
        float angle = PerlinOrientation(anchor);
        angle += angleOffset;

        Vector2 direction = new Vector2(Mathf.Sin(angle), Mathf.Cos(angle));
        Vector2 anchorXZ = new Vector2(anchor.x, anchor.z);

        anchorXZ += direction * distanceBetweenAnchors;

        float h = Terrain.activeTerrain.SampleHeight(new Vector3(anchorXZ[0], 0, anchorXZ[1]));


        return new Vector3(anchorXZ[0], h + 2, anchorXZ[1]);
    }

    float PerlinOrientation(Vector3 pos)
    {
        float offset = 24000f;

        return 2f * Mathf.PI *
            Mathf.PerlinNoise((pos.x + offset) * smooth, (pos.z + offset) * smooth);
    }

    VertexPath GeneratePath(List<Vector3> points, float vertexSpacing, bool closedPath, PathSpace space)
    {
        BezierPath bezierPath = new BezierPath(points, closedPath, space);
        return new VertexPath(bezierPath, this.transform, vertexSpacing);
    }

}
