using UnityEngine;
using PathCreation;

public class TestingPathCreator : MonoBehaviour
{
    Vector3[] anchors;
    GameObject go;
    float speed = 2f;
    float dist = 0;

    VertexPath path;
    RoadMesh roadMesh;

    void RandomAnchors()
    {
        anchors = new Vector3[5];
        for (int i = 0; i < 5; i++)
        {
            anchors[i] = new Vector3(Random.Range(-3, 3),
                Random.Range(-3, 3), Random.Range(-3, 3));
            GameObject g = GameObject.CreatePrimitive(PrimitiveType.Sphere);
            g.transform.localScale = Vector3.one * 0.2f;
            g.transform.position = anchors[i];
        }
    }

    void Start()
    {
        go = GetComponentInChildren<MeshRenderer>().gameObject;
        roadMesh = GetComponent<RoadMesh>();
        RandomAnchors();
        path = GeneratePath(anchors, false);
        roadMesh.BuildRoad(path); 
    }


    void Update()
    {
        go.transform.position = path.GetPointAtDistance(dist);
        go.transform.rotation = path.GetRotationAtDistance(dist);
        Debug.DrawRay(go.transform.position, path.GetDirectionAtDistance(dist), Color.yellow);
        Debug.DrawRay(go.transform.position, path.GetNormalAtDistance(dist), Color.blue);

        dist += speed * Time.deltaTime;
    }

    VertexPath GeneratePath(Vector3[] points, bool closedPath)
    {
        BezierPath bezierPath = new BezierPath(points, closedPath, PathSpace.xz);
        return new VertexPath(bezierPath, this.transform, 0.05f);
    }
}
