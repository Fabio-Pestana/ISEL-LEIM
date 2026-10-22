using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.AI;

public class NPC : MonoBehaviour
{
    public GameObject player;
    public Transform[] waypoints;
    public int currentTarget = -1;
    private Animator animator;
    public NavMeshAgent navMeshAgent;
    private Vector3 _distToPlayer;
    private bool isTalking = false;
    public float condition = 2f;
    private float rotationSpeed = 5.0f;
    public string npc_name;

    [SerializeField]
    private GameObject panel;

    [SerializeField]
    private TextMeshProUGUI textfield;

    [SerializeField]
    private string[] lines;

    private float speedText = 0.05f;
    private int idx = 0;

    // Start is called before the first frame update
    void Start()
    {
        navMeshAgent = GetComponent<NavMeshAgent>();
        animator = GetComponent<Animator>();
        if (panel != null)
        {
            panel.SetActive(false);
            textfield.text = string.Empty;
        }
            
    }

    private void Update()
    {
        _distToPlayer = player.transform.position - transform.position;
        if (_distToPlayer.magnitude <= condition)
        {
            _distToPlayer.y = 0;


            Quaternion targetRotation = Quaternion.LookRotation(_distToPlayer);

            transform.rotation = Quaternion.Slerp(transform.rotation, targetRotation, rotationSpeed * Time.deltaTime);
        }
        animator.SetFloat("DistPlayer", _distToPlayer.magnitude);


        if(isTalking && panel != null)
        {
            if (Input.GetMouseButtonDown(0))
            {
                if(textfield.text.Length == lines[idx].Length)
                {
                    NextLine();
                }
                else
                {
                    StopAllCoroutines();
                    textfield.text = lines[idx];
                }
            }
        }else if(!isTalking && panel != null) 
        {
            finishTalk();
        }


    }

    public void StartTalking()
    {
        if (isTalking) return;

        if(panel != null)
        {
            panel.SetActive(true);
            StartDialog();
        }
        isTalking = true;
        animator.SetBool("isTalking", true);

        if(panel == null)
        {
            StartCoroutine(StopTalkingCoroutine());
        }
    }

    public void StartDialog()
    {
        idx = 0;
        StartCoroutine(Talk());
    }

    public void NextLine()
    {
        if(idx < lines.Length - 1) 
        {
            idx++;
            textfield.text = string.Empty;
            StartCoroutine(Talk());
        }
        else
        {
            finishTalk();
        }
    }

    public void finishTalk()
    {
        panel.SetActive(false);
        isTalking = false;
        textfield.text = string.Empty;
        idx = 0;
        animator.SetBool("isTalking", false);
    }

    private IEnumerator Talk()
    {
        foreach(char c in lines[idx].ToCharArray())
        {
            textfield.text += c;
            yield return new WaitForSeconds(speedText);
        }
    }

    private IEnumerator StopTalkingCoroutine()
    {
        yield return new WaitForSeconds(10.0f); 
        isTalking = false;
        animator.SetBool("isTalking", false);
        animator.SetBool("TalkingZone", false);
    }

    public void Leave()
    {
        isTalking = false;
    }

    public void MoveToNextPoint()
    {
        currentTarget = (currentTarget + 1) % waypoints.Length;
        navMeshAgent.SetDestination(waypoints[currentTarget].position);
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.gameObject.CompareTag("NPCtalk"))
        {
            animator.SetBool("TalkingZone", true);
            StartTalking();
        }
    }
}
